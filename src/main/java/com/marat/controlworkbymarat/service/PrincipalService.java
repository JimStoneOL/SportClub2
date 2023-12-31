package com.marat.controlworkbymarat.service;


import com.marat.controlworkbymarat.entity.User;
import com.marat.controlworkbymarat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class PrincipalService {
    @Autowired
    private UserRepository userRepository;

    public User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));

    }
}