package com.fusemachine.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    private Date date;

    public enum OrderStatus {
        PENDING,
        IN_PROCESS,
        READY
    }

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToMany
//    @JsonManagedReference
//    @JoinTable(name = "order_item",
//                joinColumns = @JoinColumn(name = "order_id"),
//                inverseJoinColumns = @JoinColumn(name = "food_id"))
//    private Set<Food> foods;

    @Column(name = "total_price")
    private double totalPrice;

    public Order() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public Set<Food> getFoods() {
//        return foods;
//    }
//
//    public void setFoods(Set<Food> foods) {
//        this.foods = foods;
//    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
