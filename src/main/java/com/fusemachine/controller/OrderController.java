package com.fusemachine.controller;

import com.fusemachine.entity.Food;
import com.fusemachine.entity.Order;
import com.fusemachine.entity.User;
import com.fusemachine.service.OrderService;
import com.fusemachine.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.OrderColumn;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class OrderController {
    Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/users/{userId}/orders")
    public void save(@RequestBody Order order, @PathVariable int userId){
        User user = userService.findById(userId);
        order.setUser(user);
        order.setDate(Calendar.getInstance().getTime());
        order.setStatus(Order.OrderStatus.PENDING.name());

//        Set<Food> foods = order.getFoods();
        double totalPrice = 0;
//        for (Food food : foods) {
//            totalPrice += food.getPrice();
//        }
        order.setTotalPrice(totalPrice);
        orderService.save(order);
    }

    @GetMapping("/users/{id}/orders")
    public List<Order> findAll(@PathVariable int id){
        return orderService.findAllByUserId(id);
    }

    @GetMapping("/orders")
    public List<Order> findAll(){
        return orderService.findAll();
    }



}
