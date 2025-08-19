package com.github.Ignacio.my_Library_In_Spring.Controllers;

import com.github.Ignacio.my_Library_In_Spring.DTOs.UserRequest;
import com.github.Ignacio.my_Library_In_Spring.DTOs.UserResponse;
import com.github.Ignacio.my_Library_In_Spring.Security.Jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtUtils utils;

    @PostMapping("/signing")
    public ResponseEntity<?> singIn(@RequestBody UserRequest request){
        Authentication authentication ;

        try{
           authentication = manager.authenticate( new UsernamePasswordAuthenticationToken(
                   request.getUsername()
                   ,request.getPassword()));
        }catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails details = (UserDetails) authentication.getPrincipal();

        String token = utils.generateTokenByName(details);
        List<String> roles = details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        UserResponse response = new UserResponse(details.getUsername(), token,roles);
        return ResponseEntity.ok(response);
    }
}
