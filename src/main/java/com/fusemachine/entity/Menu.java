package com.fusemachine.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.*;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToMany
    @JsonManagedReference
    @JsonIgnore
    @JoinTable(name = "menu_item", joinColumns = @JoinColumn(name = "menu_id"),
                                    inverseJoinColumns = @JoinColumn(name = "food_id"))
    private Set<Food> foods;

    public Menu() {
    }

    public Menu(Date date) {
        this.date = date;
        this.foods = new HashSet<>();
    }

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

    public Set<Food> getFoods() {
        return foods;
    }

    public void setFoods(Set<Food> foods) {
        this.foods = foods;
    }

    public boolean addItem(Food food){
        if(this.foods == null){
            this.foods = new HashSet<>();
        }
        if(!this.foods.contains(food)) {
            this.foods.add(food);
            return true;
        }
        return false;
    }

    public boolean removeItem(Food food){
        if(this.foods.contains(food)){
            return this.foods.remove(food);
        }
        return false;
    }
}
