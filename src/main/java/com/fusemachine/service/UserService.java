package com.fusemachine.service;

import com.fusemachine.entity.User;
import com.fusemachine.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<User> findAll(){
        return userRepo.findAll();
    }

    public User findById(int id){
        return userRepo.findById(id).orElse(null);
    }

    @Transactional
    public void save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    @Transactional
    public void deleteById(int id) {
        userRepo.deleteById(id);
    }

}
