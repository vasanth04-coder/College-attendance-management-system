
package com.collegemanagement.Service;

import com.collegemanagement.Exception.UserNotFoundException;
import com.collegemanagement.Model.User;
import com.collegemanagement.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService
{
    @Autowired
    UserRepo userRepo;

    public MyUserDetailsService(UserRepo userRepo)
    {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
    {
        User user = userRepo.findByEmail(email).orElseThrow(()->new UserNotFoundException("user not found"));

        System.out.println("Loader");

        return new org.springframework.security.core.userdetails.User(
             user.getEmail(),
             user.getPassword(),
             List.of(
                    new SimpleGrantedAuthority(user.getRole().name())
             )
        );
    }
}