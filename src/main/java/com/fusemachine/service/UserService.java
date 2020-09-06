package com.fusemachine.service;

import com.fusemachine.entity.User;
import com.fusemachine.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    public List<User> findAll(){
        return userRepo.findAll();
    }

    public User findById(int id){
        return userRepo.findById(id).orElse(null);
    }

    public void save(User user){
        userRepo.save(user);
    }

    public void deleteById(int id) {
        userRepo.deleteById(id);
    }
}
