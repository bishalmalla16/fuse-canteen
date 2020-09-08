package com.fusemachine.service;

import com.fusemachine.entity.FuseUserDetails;
import com.fusemachine.entity.User;
import com.fusemachine.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FuseUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByUsername(username);
        if(!user.isPresent())
            throw new UsernameNotFoundException("User not found");
        return user.map(FuseUserDetails::new).get();
    }
}
