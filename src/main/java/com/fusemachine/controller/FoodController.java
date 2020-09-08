package com.fusemachine.controller;

import com.fusemachine.entity.Food;
import com.fusemachine.exceptions.ResourcesNotFoundException;
import com.fusemachine.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @GetMapping
    public List<Food> findAll(){
        return foodService.findAll();
    }

    @GetMapping("/{id}")
    public Food findById(@PathVariable int id){
        Food food = foodService.findById(id);
        if(food == null){
            throw new ResourcesNotFoundException("Item with id = " + id + " not found.");
        }
        return food;
    }

    @PostMapping
    public void save(@RequestBody Food food){
        food.setId(0);
        foodService.save(food);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody Food food, @PathVariable int id){
        if(foodService.findById(id) == null){
            throw new ResourcesNotFoundException("Item with id = " + id + " not found.");
        }
        food.setId(id);
        foodService.save(food);
    }

    @DeleteMapping("/{id}")
    public  void  deleteById(@PathVariable int id){
        if(foodService.findById(id) == null){
            throw new ResourcesNotFoundException("Item with id = " + id + " not found.");
        }
        foodService.deleteById(id);
    }
}
