package com.github.Ignacio.my_Library_In_Spring.Service.interfaces;

import com.github.Ignacio.my_Library_In_Spring.DTOs.UserRequest;
import com.github.Ignacio.my_Library_In_Spring.DTOs.UserResponse;

import java.util.List;
import java.util.Map;

public interface UserServiceInterface {
    UserResponse getUserById(Long id);
    List<UserResponse> getAllUsers();

    UserResponse post(UserRequest input);
    boolean deleteUserById(Long id);

    UserResponse putUserById(Long id,UserRequest update);
    UserResponse patchUserById(Long id, Map<String,Object> update);
}
