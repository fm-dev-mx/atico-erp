package com.atico.erp.core.backend.services;

import com.atico.erp.core.backend.entities.User;
import com.atico.erp.core.backend.repositories.UserRepository;
import com.atico.erp.core.backend.security.AppUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.getByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }
        return new AppUserDetails(user);
    }

    public User getLoggedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof UserDetails) {
            String userName = ((UserDetails) principal).getUsername();
            return userRepository.getByUsername(userName);
        }
        return null;
    }

    public String getLoggedUsername() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
