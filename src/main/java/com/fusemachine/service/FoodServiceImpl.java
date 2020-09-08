package com.fusemachine.service;

import com.fusemachine.entity.Food;
import com.fusemachine.repo.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodRepository foodRepo;

    @Override
    @Transactional
    public Food findById(int id) {
        return foodRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Food findByName(String name) {
        return foodRepo.findByName(name).orElse(null);
    }

    @Override
    @Transactional
    public List<Food> findAll() {
        return foodRepo.findAll();
    }

    @Override
    @Transactional
    public void save(Food food) {
        foodRepo.save(food);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        foodRepo.deleteById(id);
    }
}
