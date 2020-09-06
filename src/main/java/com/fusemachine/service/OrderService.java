package com.fusemachine.service;

import com.fusemachine.entity.Order;
import com.fusemachine.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    public void save(Order order){
        orderRepo.save(order);
    }

    public List<Order> findAll(){
        return orderRepo.findAll();
    }

    public List<Order> findAllByUserId(int id) {
        return orderRepo.findAllByUserId(id);
    }

//    public double findTotalPrice(int id){
//        return orderRepo.findTotalPrice(id);
//    }

}
