package com.sathish.controller;

import com.sathish.model.User;
import com.sathish.repository.UserRepository;
import com.sathish.service.CustomerUserServiceImpl;
import com.sathish.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) {
        User user = userService.getUserProfiles(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
       List<User> allUsers=  userService.getAllUser();
       return new ResponseEntity<>(allUsers,HttpStatus.OK);
    }
}
