package com.sharefood.ShareFood.repository;

import com.sharefood.ShareFood.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Query(value = "SELECT * FROM tbl_users WHERE user_name = ?1 AND password = ?2 AND locked = 0", nativeQuery = true)
    User findUserByUserNameAndPass(String username, String pass);

    List<User> findAllByPermission(int permission);

    List<User> findAll();

    User findUserById(int id);

    @Query(value = "SELECT * FROM tbl_users WHERE user_name = ?1", nativeQuery = true)
    User forgotPassword(String userName);

}
