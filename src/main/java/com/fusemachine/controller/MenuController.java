package com.fusemachine.controller;

import com.fusemachine.entity.Food;
import com.fusemachine.entity.Menu;
import com.fusemachine.exceptions.ItemNotFoundException;
import com.fusemachine.exceptions.MenuNotFoundException;
import com.fusemachine.service.FoodService;
import com.fusemachine.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @Autowired
    private FoodService foodService;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


    @InitBinder
    public void initBinder(WebDataBinder dataBinder) throws Exception {
        CustomDateEditor dateEditor = new CustomDateEditor(formatter, true){
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if(text.equals("today"))
                    setValue(new Date());
                else {
                    try {
                        super.setAsText(text);
                    }catch (IllegalArgumentException ex){
                        logger.error("Illegal Exception: " + ex.getMessage());
                    }
                }
            }
        };
        dataBinder.registerCustomEditor(Date.class, dateEditor);
    }

    @GetMapping
    public Menu findByDate(@RequestParam(required = false, defaultValue = "today") Date date){
        logger.info("Menu date: " + date);
        Menu menu = menuService.findByDate(date);
        if(menu == null){
            throw new MenuNotFoundException("Menu not found for date : " + formatter.format(date));
        }
        return menu;
    }

    @GetMapping("/{id}")
    public Menu findById(@PathVariable int id){
        Menu menu = menuService.findById(id);
        if(menu == null){
            throw new MenuNotFoundException("Menu with id = " + id + " not found.");
        }
        return menu;
    }

    @PostMapping
    public void save(@RequestBody Menu menu){
        Menu todayMenu = menuService.findByDate(Calendar.getInstance().getTime());
        if(todayMenu != null) {
            //To be changed to MenuAlreadyExistException
            throw new MenuNotFoundException("Menu Already Exist for today.");
        }
        menu.setDate(Calendar.getInstance().getTime());
        menuService.save(menu);
    }

    @PutMapping
    public void updateMenu(@RequestBody Menu menu){
        Menu todayMenu = menuService.findByDate(Calendar.getInstance().getTime());
        if(todayMenu == null) {
            throw new MenuNotFoundException("Menu not found for today.");
        }
        menu.setId(todayMenu.getId());
        menu.setDate(todayMenu.getDate());
        menuService.save(menu);
    }


    @GetMapping("/foods")
    public Set<Food> findAllItemsById(){
        Menu menu = menuService.findByDate(Calendar.getInstance().getTime());
        if(menu == null){
            throw new MenuNotFoundException("Today's Menu not found.");
        }
        return menuService.findFoodsById(menu.getId());
    }

    @PostMapping("/foods")
    public void addItem(@RequestParam(defaultValue = "") String item){
        Menu menu = menuService.findByDate(Calendar.getInstance().getTime());
        if(menu == null){
            throw new MenuNotFoundException("Today's menu not found.");
        }
        Food food = foodService.findByName(item);
        if(food == null){
            throw new ItemNotFoundException("Item with name = " + item + " not found.");
        }
        if(!menuService.addItem(menu, food)){
            //To be changed
            throw new ItemNotFoundException("Item with name = " + item + " already in menu.");
        }
    }

    @DeleteMapping("/foods")
    public void removeItem(@RequestParam(defaultValue = "") String item){
        Menu menu = menuService.findByDate(Calendar.getInstance().getTime());
        if(menu == null){
            throw new MenuNotFoundException("Today's menu not found.");
        }
        Food food = foodService.findByName(item);
        if(food == null || !menuService.removeItem(menu, food)){
            throw new ItemNotFoundException("Item with name = " + item + " not available in menu.");
        }
    }
}
