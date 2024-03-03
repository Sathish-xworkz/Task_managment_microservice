package com.sathish.controller;

import com.sathish.config.JwtProvider;
import com.sathish.model.User;
import com.sathish.repository.UserRepository;
import com.sathish.request.LoginReqest;
import com.sathish.response.AuthResponse;
import com.sathish.service.CustomerUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerUserServiceImpl customerUserServiceImpl;
    @Autowired
    private UserDetailsService userDetailsService;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
        String email= user.getEmail();
        String password= user.getPassword();
        String fullName= user.getFullName();
        String role= user.getRole();

        User isEmailExist = userRepository.findByEmail(email);
        if(isEmailExist !=null){
            throw new Exception("Email is already used with another account");
        }
        User createdUser=new User();

        createdUser.setEmail(email);
        createdUser.setFullName(fullName);
        createdUser.setRole(role);
        createdUser.setPassword(passwordEncoder.encode(password));

        User savedUser= userRepository.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);
        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Register sucessfully");
        authResponse.setStatus(true);
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);

    }
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginReqest loginReqest){

        String username= loginReqest.getEmail();
        String password= loginReqest.getPassword();
        System.out.println(username+"------"+password);

        Authentication authentication =  authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = JwtProvider.generateToken(authentication);
        AuthResponse authResponse=new AuthResponse();

        authResponse.setMessage("Login Success");
        authResponse.setJwt(token);
        authResponse.setStatus(true);

        return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customerUserServiceImpl.loadUserByUsername(username);
        System.out.println("Sign in userDetails:"+userDetails);
        if(userDetails==null){
            System.out.println("Sign in userDetails null "+userDetails);
            throw new BadCredentialsException("Invalid user or password..");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            System.out.println("sign is userDetails..."+userDetails);
            throw new BadCredentialsException("invalid user or password..");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

    }

}
