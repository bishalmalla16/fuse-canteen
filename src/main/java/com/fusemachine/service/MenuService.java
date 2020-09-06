package com.fusemachine.service;

import com.fusemachine.entity.Food;
import com.fusemachine.entity.Menu;
import com.fusemachine.repo.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    public Menu findByDate(Date date){
        return menuRepo.findByDateEquals(date).orElse(null);
    }

    public void save(Menu menu){
        menuRepo.save(menu);
    }

    public boolean addItem(int menuId, Food food){
        Menu menu = findById(menuId);
        if(!menu.addItem(food)){
            return false;
        }
        menuRepo.save(menu);
        return true;
    }

    public boolean removeItem(int menuId, Food food){
        Menu menu = findById(menuId);
        if(!menu.removeItem(food)){
            return false;
        }
        menuRepo.save(menu);
        return true;
    }
}
