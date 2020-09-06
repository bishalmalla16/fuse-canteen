package com.fusemachine.service;

import com.fusemachine.entity.Food;
import com.fusemachine.entity.Menu;
import com.fusemachine.repo.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepo;

    public List<Food> findFoodsById(int id){
        Menu menu = findById(id);
        return menu.getFoods();
    }

    public Menu findById(int id){
        return menuRepo.findById(id).orElse(null);
    }

    public void save(Menu menu){
        menuRepo.save(menu);
    }

    public void addItem(int menuId, Food food){
        Menu menu = findById(menuId);
        menu.addItem(food);
        menuRepo.save(menu);
    }

    public void removeItem(int menuId, Food food){
        Menu menu = findById(menuId);
        menu.removeItem(food);
        menuRepo.save(menu);
    }
}
