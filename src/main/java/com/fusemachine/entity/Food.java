package com.fusemachine.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@JsonIgnoreProperties("orders")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private double price;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "menu_item",
                joinColumns = @JoinColumn(name = "food_id"),
                inverseJoinColumns = @JoinColumn(name = "menu_id"))
    private Set<Menu> menus;

    @ManyToMany
    @JsonBackReference
    @JoinTable(name = "order_item",
            joinColumns = @JoinColumn(name = "food_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id"))
    private Set<UserOrder> orders;

    public Food(){}

    public Food(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }

    public Set<UserOrder> getOrders() {
        return orders;
    }

    public void setOrders(Set<UserOrder> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return id == food.id &&
                Objects.equals(name, food.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
