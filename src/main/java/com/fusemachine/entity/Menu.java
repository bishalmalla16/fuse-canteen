package com.fusemachine.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date date;

    @ManyToMany
    @JoinTable(name = "menu_items", joinColumns = @JoinColumn(name = "menu_id"),
                                    inverseJoinColumns = @JoinColumn(name = "food_id"))
    private List<Food> foods;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public void addItem(Food food){
        if(this.foods == null){
            this.foods = new ArrayList<>();
        }
        if(!this.foods.contains(food)) {
            this.foods.add(food);
        }
    }

    public boolean removeItem(Food food){
        if(this.foods.contains(food)){
            return this.foods.remove(food);
        }
        return false;
    }
}
