package com.sharefood.ShareFood.service.impl;

import com.sharefood.ShareFood.model.Manager;
import com.sharefood.ShareFood.model.User;
import com.sharefood.ShareFood.repository.ManagerRepository;
import com.sharefood.ShareFood.service.base.ManagerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public Manager findManagerById(int id) {
        return managerRepository.findManagerById(id);
    }

    @Override
    public Manager findManagerByUser(User user) {
        return managerRepository.findManagerByUser(user);
    }

    @Override
    public Manager insert(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public Manager update(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public void delete(int id) {
        managerRepository.deleteById(id);
    }

    @Override
    public Collection<Manager> gettAll() {
        return (Collection<Manager>) managerRepository.findAll();
    }
}
