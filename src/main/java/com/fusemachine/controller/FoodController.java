package com.fusemachine.controller;

import com.fusemachine.entity.Food;
import com.fusemachine.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FoodController {

    @Autowired
    private FoodService foodService;

    @GetMapping("/foods")
    public List<Food> findAll(){
        return foodService.findAll();
    }

    @GetMapping("/foods/{id}")
    public Food findById(@PathVariable int id){
        return foodService.findById(id);
    }

    @PostMapping("/foods")
    public void save(@RequestBody Food food){
        food.setId(0);
        foodService.save(food);
    }

    @PutMapping("/foods/{id}")
    public void update(@RequestBody Food food, @PathVariable int id){
        food.setId(id);
        foodService.save(food);
    }
}
