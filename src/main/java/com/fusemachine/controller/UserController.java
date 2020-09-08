package com.fusemachine.controller;

import com.fusemachine.entity.User;
import com.fusemachine.exceptions.NotFoundException;
import com.fusemachine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> findAll(){
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable int id){
        User user = userService.findById(id);
        if(user == null) {
            throw new NotFoundException("User with id = " + id + " not found.");
        }
        return user;
    }

    @PostMapping
    public void save(@RequestBody User user){
        userService.save(user);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable int id){
        userService.deleteById(id);
    }

}
