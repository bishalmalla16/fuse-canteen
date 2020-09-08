package com.fusemachine.controller;

import com.fusemachine.entity.UserOrder;
import com.fusemachine.entity.User;
import com.fusemachine.exceptions.CannotAlterException;
import com.fusemachine.exceptions.InvalidArgumentException;
import com.fusemachine.exceptions.ResourcesNotFoundException;
import com.fusemachine.service.FoodService;
import com.fusemachine.service.OrderService;
import com.fusemachine.service.UserService;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodService foodService;

    private static final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) throws Exception {
        CustomDateEditor dateEditor = new CustomDateEditor(dateFormatter, true){
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if(text.equals("today"))
                    setValue(new Date());
                else {
                    try {
                        super.setAsText(text);
                    }catch (IllegalArgumentException ex){
                        logger.error("Illegal Exception: " + ex.getMessage());
                    }
                }
            }
        };
        dataBinder.registerCustomEditor(Date.class, dateEditor);
    }

    @GetMapping("/orders")
    public List<UserOrder> findAll(@RequestParam(required = false, defaultValue = "today") Date date){
        Date startTime;
        Date endTime;
        try {
            startTime = dateTimeFormatter.parse(dateFormatter.format(date) + " 00:00:00");
            endTime = dateTimeFormatter.parse(dateFormatter.format(date) + " 23:59:59");
            return orderService.findAllByScheduledBetween(startTime, endTime);
        }catch (ParseException ex){
            logger.error(ex.getMessage());
            return null;
        }
    }

    @GetMapping("/orders/{id}")
    public UserOrder findById(@PathVariable int id){
        UserOrder order = orderService.findById(id);
        if(order == null){
            throw new ResourcesNotFoundException("Order with id = " + id + " not found.");
        }
        return order;
    }

    @PutMapping("/orders/{id}")
    public void updateOrderStatus(@PathVariable int id, @RequestBody JSONObject object){
        UserOrder order = orderService.findById(id);
        if(order == null){
            throw new ResourcesNotFoundException("Order with id = " + id + " not found.");
        }
        String statusName = object.get("status").toString();
        UserOrder.OrderStatus status;
        if(statusName.equalsIgnoreCase("PENDING"))
            status = UserOrder.OrderStatus.PENDING;
        else if(statusName.equalsIgnoreCase("IN_PROCESS"))
            status = UserOrder.OrderStatus.IN_PROCESS;
        else if(statusName.equalsIgnoreCase("READY"))
            status = UserOrder.OrderStatus.READY;
        else
            throw new InvalidArgumentException("Invalid Status");

        order.setStatus(status.name());
        orderService.save(order);
    }

    @DeleteMapping("/orders/{id}")
    public void deleteById(@PathVariable int id){
        UserOrder order = orderService.findById(id);
        if(order == null){
            throw new ResourcesNotFoundException("Order with id = " + id + " not found.");
        }
        orderService.deleteById(id);
    }

    @GetMapping("/users/{userId}/orders")
    public List<UserOrder> findAll(@PathVariable int userId, @RequestParam(required = false, defaultValue = "today") Date date){
        User user = userService.findById(userId);
        if(user == null){
            throw new ResourcesNotFoundException("User with id = " + userId + " not found.");
        }
        Date startTime;
        Date endTime;
        try {
            startTime = dateTimeFormatter.parse(dateFormatter.format(date) + " 00:00:00");
            endTime = dateTimeFormatter.parse(dateFormatter.format(date) + " 23:59:59");
            return orderService.findAllByUserIdAndScheduledBetween(userId, startTime, endTime);
        }catch (ParseException ex){
            logger.error(ex.getMessage());
            return null;
        }
    }

    @GetMapping("/users/{userId}/orders/{id}")
    public UserOrder findByOrderId(@PathVariable int userId, @PathVariable int id){
        User user = userService.findById(userId);
        if(user == null){
            throw new ResourcesNotFoundException("User with id = " + userId + " not found.");
        }
        UserOrder curOrder = orderService.findById(id);
        if(curOrder == null || curOrder.getUser() != user){
            throw new ResourcesNotFoundException("This User doesn't have order with id = " + id);
        }
        return orderService.findById(id);
    }

    @PostMapping("/users/{userId}/orders")
    public void save(@RequestBody UserOrder order, @PathVariable int userId){
        User user = userService.findById(userId);
        if(user == null){
            throw new ResourcesNotFoundException("User with id = " + userId + " not found.");
        }
        if(order.getOrderItems().isEmpty())
            throw new InvalidArgumentException("Can't place an empty order.");

        order.setCreatedAt(Calendar.getInstance().getTime());
        orderService.save(user, order);
    }

    @PutMapping("/users/{userId}/orders/{id}")
    public void updateOrder(@PathVariable int userId, @PathVariable int id, @RequestBody UserOrder order){
        User user = userService.findById(userId);
        if(user == null){
            throw new ResourcesNotFoundException("User with id = " + userId + " not found.");
        }
        UserOrder curOrder = orderService.findById(id);
        if(curOrder == null || curOrder.getUser() != user){
            throw new ResourcesNotFoundException("This User doesn't have order with id = " + id);
        }
        if(!curOrder.getStatus().equals("PENDING"))
            throw new CannotAlterException("Order cannot be altered now.");
        if(order.getOrderItems().isEmpty())
            throw new InvalidArgumentException("Can't place an empty order.");

        order.setId(id);
        orderService.update(curOrder, order);
    }

    @DeleteMapping("/users/{userId}/orders/{id}")
    public void deleteOrder(@PathVariable int userId, @PathVariable int id){
        User user = userService.findById(userId);
        if(user == null){
            throw new ResourcesNotFoundException("User with id = " + userId + " not found.");
        }
        UserOrder curOrder = orderService.findById(id);
        if(curOrder == null || curOrder.getUser().getId() != userId){
            throw new ResourcesNotFoundException("This User doesn't have order with id = " + id);
        }
        if(!curOrder.getStatus().equals("PENDING"))
            throw new CannotAlterException("Order cannot be altered");
        orderService.deleteById(id);
    }

}
