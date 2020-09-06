package com.fusemachine.controller;

import com.fusemachine.entity.Food;
import com.fusemachine.exceptions.ItemNotFoundException;
import com.fusemachine.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @GetMapping("/foods")
    public List<Food> findAll(){
        return foodService.findAll();
    }

    @GetMapping("/foods/{id}")
    public Food findById(@PathVariable int id){
        Food food = foodService.findById(id);
        if(food == null){
            throw new ItemNotFoundException("Item with id = " + id + " not found.");
        }
        return food;
    }

    @PostMapping("/foods")
    public void save(@RequestBody Food food){
        food.setId(0);
        foodService.save(food);
    }

    @PutMapping("/foods/{id}")
    public void update(@RequestBody Food food, @PathVariable int id){
        if(foodService.findById(id) == null){
            throw new ItemNotFoundException("Item with id = " + id + " not found.");
        }
        food.setId(id);
        foodService.save(food);
    }

    @DeleteMapping("/foods/{id}")
    public  void  deleteById(@PathVariable int id){
        if(foodService.findById(id) == null){
            throw new ItemNotFoundException("Item with id = " + id + " not found.");
        }
        foodService.deleteById(id);
    }
}
