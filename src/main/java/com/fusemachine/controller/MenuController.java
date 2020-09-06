package com.fusemachine.controller;

import com.fusemachine.entity.Food;
import com.fusemachine.entity.Menu;
import com.fusemachine.service.FoodService;
import com.fusemachine.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private FoodService foodService;

    @GetMapping("/menus/{id}")
    public Menu findById(@PathVariable int id){
        return menuService.findById(id);
    }

    @GetMapping("/menus/{id}/foods")
    public List<Food> findAllItemsById(@PathVariable int id){
        return menuService.findFoodsById(id);
    }

    @PostMapping("/menus")
    public void save(@RequestBody Menu menu){
        menuService.save(menu);
    }

    @PostMapping("/menus/{id}/foods")
    public void addItem(@PathVariable int id, @RequestParam(defaultValue = "") String itemName){
        Food food = foodService.findByName(itemName);
        menuService.addItem(id, food);
    }

    @DeleteMapping("/menus/{id}/foods")
    public void removeItem(@PathVariable int id, @RequestParam(defaultValue = "") String itemName){
        Food food = foodService.findByName(itemName);
        menuService.removeItem(id, food);
    }
}
