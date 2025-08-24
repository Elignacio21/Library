package com.github.Ignacio.my_Library_In_Spring.Service.interfaces;

import com.github.Ignacio.my_Library_In_Spring.DTOs.Mapper;
import com.github.Ignacio.my_Library_In_Spring.DTOs.UserRequest;
import com.github.Ignacio.my_Library_In_Spring.DTOs.UserResponse;
import com.github.Ignacio.my_Library_In_Spring.Entity.User;
import com.github.Ignacio.my_Library_In_Spring.Repository.RepositoryUser;

import java.util.List;
import java.util.Map;

public class UserService implements UserServiceInterface{

    private RepositoryUser repositoryUser;

    private Mapper mapper;

    @Override
    public UserResponse getUserById(Long id) {
        return null;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return List.of();
    }

    @Override
    public UserResponse post(UserRequest input) {
        return null;
    }

    @Override
    public boolean deleteUserById(Long id) {
        return false;
    }

    @Override
    public UserResponse putUserById(Long id, UserRequest update) {
        return null;
    }

    @Override
    public UserResponse patchUserById(Long id, Map<String, Object> update) {
        return null;
    }
}
