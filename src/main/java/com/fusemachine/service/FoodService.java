package com.fusemachine.service;

import com.fusemachine.entity.Food;

import java.util.List;

public interface FoodService {

    Food findById(int id);
    Food findByName(String name);
    List<Food> findAll();
    void save(Food food);
    void deleteById(int id);


}
