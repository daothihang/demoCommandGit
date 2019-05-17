package com.sharefood.ShareFood.repository;

import com.sharefood.ShareFood.model.Manager;
import com.sharefood.ShareFood.model.User;
import org.springframework.data.repository.CrudRepository;

public interface ManagerRepository extends CrudRepository<Manager, Integer> {

    Manager findManagerById(int id);

    Manager findManagerByUser(User user);

}
