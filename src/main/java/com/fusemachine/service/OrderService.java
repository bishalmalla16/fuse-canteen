package com.fusemachine.service;

import com.fusemachine.entity.Food;
import com.fusemachine.entity.User;
import com.fusemachine.entity.UserOrder;
import com.fusemachine.exceptions.NotFoundException;
import com.fusemachine.repo.FoodRepository;
import com.fusemachine.repo.OrderRepository;
import com.fusemachine.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private FoodRepository foodRepo;

    public UserOrder findById(int id){
        return orderRepo.findById(id).orElse(null);
    }

    public List<UserOrder> findAll(){
        return orderRepo.findAll();
    }

    public List<UserOrder> findAllByUserId(int userId){
        return orderRepo.findAllByUserId(userId);
    }

    public List<UserOrder> findAllByUserIdAndDate(int userId, Date date) {
        return orderRepo.findAllByUserIdAndDateEquals(userId, date);
    }

    public void save(UserOrder userOrder){
        orderRepo.save(userOrder);
    }

    public void save(User user, UserOrder order) {
        order.setUser(user);
        order.setStatus(UserOrder.OrderStatus.PENDING.name());

        Set<Food> foods = order.getFoods();
        double totalPrice = 0;
        for (Food food : foods) {
            if(!foodRepo.findById(food.getId()).isPresent())
                throw new NotFoundException("Food with id = " + food.getId() + " not found.");
            food = foodRepo.findById(food.getId()).get();
            totalPrice += food.getPrice();
        }
        order.setTotalPrice(totalPrice);
        orderRepo.save(order);
    }

    public void deleteById(int id) {
        orderRepo.deleteById(id);
    }

    public List<UserOrder> findAllByUserIdAndDateBetween(int id, Date startTime, Date endTime) {
        return orderRepo.findAllByUserIdAndDateBetween(id, startTime, endTime);
    }

    public List<UserOrder> findAllByDateBetween(Date startTime, Date endTime) {
        return orderRepo.findAllByDateBetween(startTime, endTime);
    }

//    public double findTotalPrice(int id){
//        return orderRepo.findTotalPrice(id);
//    }

}
