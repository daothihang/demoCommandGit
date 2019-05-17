package com.sharefood.ShareFood.service.base;

import com.sharefood.ShareFood.model.Manager;
import com.sharefood.ShareFood.model.User;

public interface ManagerService extends Service<Manager> {
    Manager findManagerById(int id);
    Manager findManagerByUser(User user);
}
