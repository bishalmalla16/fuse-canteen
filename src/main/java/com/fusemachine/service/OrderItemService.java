package com.fusemachine.service;

import com.fusemachine.entity.OrderItem;
import com.fusemachine.repo.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepo;

    @Transactional
    public void deleteById(int id) {
        orderItemRepo.deleteById(id);
    }

    public OrderItem findById(int id){
        return orderItemRepo.findById(id).orElse(null);
    }

    @Transactional
    public void save(OrderItem orderItem) {
        orderItemRepo.save(orderItem);
    }
}
