package com.fusemachine.service;

import com.fusemachine.entity.Food;
import com.fusemachine.entity.OrderItem;
import com.fusemachine.entity.User;
import com.fusemachine.entity.UserOrder;
import com.fusemachine.exceptions.InvalidArgumentException;
import com.fusemachine.exceptions.ResourcesNotFoundException;
import com.fusemachine.repo.FoodRepository;
import com.fusemachine.repo.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private FoodRepository foodRepo;

    @Autowired
    private OrderItemService orderItemService;

    public UserOrder findById(int id){
        return orderRepo.findById(id).orElse(null);
    }

    public List<UserOrder> findAll(){
        return orderRepo.findAll();
    }

    public List<UserOrder> findAllByUserId(int userId){
        return orderRepo.findAllByUserId(userId);
    }

    public List<UserOrder> findAllByUserIdAndScheduledBetween(int id, Date startTime, Date endTime) {
        return orderRepo.findAllByUserIdAndScheduledAtBetween(id, startTime, endTime, Sort.by("scheduledAt").and(Sort.by("createdAt")));
    }

    public List<UserOrder> findAllByScheduledBetween(Date startTime, Date endTime) {
        return orderRepo.findAllByScheduledAtBetween(startTime, endTime, Sort.by("scheduledAt").and(Sort.by("createdAt")));
    }

    @Transactional
    public void save(UserOrder userOrder){
        orderRepo.save(userOrder);
    }

    @Transactional
    public void save(User user, UserOrder order) {
        order.setUser(user);
        order.setStatus(UserOrder.OrderStatus.PENDING.name());

        Set<OrderItem> items = order.getOrderItems();
        double totalPrice = 0;
        for (OrderItem item : items) {
            item.setOrder(order);
            Food food = item.getFood();
            if(food == null)
                throw new ResourcesNotFoundException("Food not found.");

            if(!foodRepo.findById(food.getId()).isPresent())
                throw new ResourcesNotFoundException("Food with id = " + food.getId() + " not found.");

            int quantity = item.getQuantity();
            if(quantity <= 0)
                throw new InvalidArgumentException("Quantity cannot be less or equals to 0.");

            food = foodRepo.findById(food.getId()).get();
            totalPrice += food.getPrice() * quantity;
        }
        order.setTotalPrice(totalPrice);
        orderRepo.save(order);
    }

    @Transactional
    public void update(UserOrder curOrder, UserOrder order) {
        curOrder.setScheduledAt(order.getScheduledAt());

        Set<OrderItem> items = order.getOrderItems();
        Set<OrderItem> curItems = curOrder.getOrderItems();
        double totalPrice = 0;

        for (OrderItem item : items) {
            Food food = item.getFood();

            if(food == null)
                throw new ResourcesNotFoundException("Food not found.");

            if(!foodRepo.findById(food.getId()).isPresent())
                throw new ResourcesNotFoundException("Food with id = " + food.getId() + " not found.");

            item.setOrder(curOrder);

            food = foodRepo.findById(food.getId()).get();
            item.setFood(food);
            int quantity = item.getQuantity();
            if(quantity <= 0)
                throw new InvalidArgumentException("Quantity cannot be less or equals to 0.");

            totalPrice += food.getPrice() * quantity;
        }

        for(OrderItem curItem : curItems){
            boolean contained = false;
            for(OrderItem item : items){
                if(curItem.equals(item)){
                    item.setId(curItem.getId());
                    contained = true;
                    break;
                }
            }
            if(!contained){
                orderItemService.deleteById(curItem.getId());
            }
        }

        for (OrderItem item : items) {
            orderItemService.save(item);
        }

        curOrder.setTotalPrice(totalPrice);
        curOrder.setOrderItems(items);
        orderRepo.save(curOrder);
    }

    @Transactional
    public void deleteById(int id) {
        orderRepo.deleteById(id);
    }

}
