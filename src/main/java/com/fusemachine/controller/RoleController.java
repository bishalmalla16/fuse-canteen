package com.fusemachine.controller;

import com.fusemachine.entity.Role;
import com.fusemachine.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public List<Role> findAll(){
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    public Role findById(@PathVariable int id){
        return roleService.findById(id);
    }

    @PostMapping
    public void save(@RequestBody Role role){
        roleService.save(role);
    }

}
