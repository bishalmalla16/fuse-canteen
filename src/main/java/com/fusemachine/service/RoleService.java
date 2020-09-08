package com.fusemachine.service;

import com.fusemachine.entity.Role;
import com.fusemachine.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepo;

    public List<Role> findAll(){
        return roleRepo.findAll();
    }

    public Role findById(int id){
        return roleRepo.findById(id).orElse(null);
    }

    @Transactional
    public void save(Role role){
        roleRepo.save(role);
    }

    @Transactional
    public void deleteById(int id){
        roleRepo.deleteById(id);
    }
}
