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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @Autowired
    private FoodService foodService;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        CustomDateEditor dateEditor = new CustomDateEditor(dateFormat, true){
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if(text.equals("today"))
                    setValue(new Date());
                else
                    super.setAsText(text);
            }
        };
        dataBinder.registerCustomEditor(Date.class, dateEditor);
    }

    @GetMapping("/menus")
    private Menu findByDate(@RequestParam(required = false, defaultValue = "today") Date date){
        Menu menu = menuService.findByDate(date);
        if(menu == null){
            throw new MenuNotFoundException("Menu not found for today.");
        }
        return menu;
    }

    @GetMapping("/menus/{id}")
    public Menu findById(@PathVariable int id){
        Menu menu = menuService.findById(id);
        if(menu == null){
            throw new MenuNotFoundException("Menu with id = " + id + " not found.");
        }
        return menu;
    }

    @GetMapping("/menus/{id}/foods")
    public Set<Food> findAllItemsById(@PathVariable int id){
        Menu menu = menuService.findById(id);
        if(menu == null){
            throw new MenuNotFoundException("Menu with id = " + id + " not found.");
        }
        return menuService.findFoodsById(id);
    }

    @PostMapping("/menus")
    public void save(){
        Menu menu = menuService.findByDate(Calendar.getInstance().getTime());
        if(menu != null) {

            //To be changed to MenuAlreadyExistException
            throw new MenuNotFoundException("Menu Already Exist for today.");
        }else{
            menu = new Menu(Calendar.getInstance().getTime());
            menuService.save(menu);
        }
    }

    @PostMapping("/menus/{id}/foods")
    public void addItem(@PathVariable int id, @RequestParam(defaultValue = "") String item){
        Menu menu = menuService.findById(id);
        if(menu == null){
            throw new MenuNotFoundException("Menu with id = " + id + " not found.");
        }
        Food food = foodService.findByName(item);
        if(food == null){
            throw new ItemNotFoundException("Item with name = " + item + " not found.");
        }
        if(!menuService.addItem(id, food)){
            //To be changed
            throw new ItemNotFoundException("Item with name = " + item + " already in menu.");
        }
    }

    @DeleteMapping("/menus/{id}/foods")
    public void removeItem(@PathVariable int id, @RequestParam(defaultValue = "") String item){
        Menu menu = menuService.findById(id);
        if(menu == null){
            throw new MenuNotFoundException("Menu with id = " + id + " not found.");
        }
        Food food = foodService.findByName(item);
        if(food == null || !menuService.removeItem(id, food)){
            throw new ItemNotFoundException("Item with name = " + item + " not available in menu.");
        }
    }
}
