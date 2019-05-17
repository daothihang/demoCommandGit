package com.sharefood.ShareFood.service.base;

import com.sharefood.ShareFood.model.Restaurant;

import java.util.List;

public interface RestaurantService extends Service<Restaurant> {
    List<Restaurant> findWithPage(int page);

    int countAll();

    Restaurant findRestaurantById(int id);

    int blockRestaurant(int id);

    Restaurant findRestaurantById_restaurant(int id);

    int unblockRestaurant(int id);
}
