package com.sharefood.ShareFood.service.base;

import com.sharefood.ShareFood.model.User;

import java.util.List;

public interface UserService extends Service<User> {
    User findUserByUserNameAndPass(String username, String pass);
    List<User> findAllByPermission(int permission);
    User findUserById(int id);
    List<User> findAll();
    User forgotPassword(String userName);
}
