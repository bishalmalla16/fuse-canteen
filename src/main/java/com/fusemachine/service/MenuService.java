package com.fusemachine.service;

import com.fusemachine.entity.Food;
import com.fusemachine.entity.Menu;
import com.fusemachine.repo.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepo;

    public Set<Food> findFoodsById(int id){
        Menu menu = findById(id);
        return menu.getFoods();
    }

    public Menu findById(int id){
        return menuRepo.findById(id).orElse(null);
    }

    public Menu findByDate(Date date){
        return menuRepo.findByDateEquals(date).orElse(null);
    }

    @Transactional
    public void save(Menu menu){
        menuRepo.save(menu);
    }

    @Transactional
    public boolean addItem(Menu menu, Food food){
        if(!menu.addItem(food)){
            return false;
        }
        menuRepo.save(menu);
        return true;
    }

    @Transactional
    public boolean removeItem(Menu menu, Food food){
        if(!menu.removeItem(food)){
            return false;
        }
        menuRepo.save(menu);
        return true;
    }
}
