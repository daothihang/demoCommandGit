package com.sharefood.ShareFood.service.impl;

import com.sharefood.ShareFood.model.User;
import com.sharefood.ShareFood.repository.UserRepository;
import com.sharefood.ShareFood.service.base.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserByUserNameAndPass(String username, String pass) {
        return userRepository.findUserByUserNameAndPass(username, pass);
    }

    @Override
    public List<User> findAllByPermission(int permission) {
        return userRepository.findAllByPermission(permission);
    }

    @Override
    public User findUserById(int id) {
        return userRepository.findUserById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User forgotPassword(String userName) {
        return userRepository.forgotPassword(userName);
    }

    @Override
    public User insert(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public Collection<User> gettAll() {
        return (Collection<User>) userRepository.findAll();
    }
}
