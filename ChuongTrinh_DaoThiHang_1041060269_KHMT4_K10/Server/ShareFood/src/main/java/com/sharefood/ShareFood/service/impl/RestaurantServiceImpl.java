package com.sharefood.ShareFood.service.impl;

import com.sharefood.ShareFood.model.Restaurant;
import com.sharefood.ShareFood.repository.RestaurantRepository;
import com.sharefood.ShareFood.service.base.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public List<Restaurant> findWithPage(int page) {
        int limit = 5;
        int offset = limit*(page - 1);
        return restaurantRepository.findWithPage(offset);
    }

    @Override
    public int countAll() {
        return restaurantRepository.countAll();
    }

    @Override
    public Restaurant findRestaurantById(int id) {
        return restaurantRepository.findRestaurantById(id);
    }

    @Override
    public int blockRestaurant(int id) {
        return restaurantRepository.blockRestaurant(id);
    }

    @Override
    public Restaurant findRestaurantById_restaurant(int id) {
        return restaurantRepository.findRestaurantById(id);
    }

    @Override
    public int unblockRestaurant(int id) {
        return restaurantRepository.unblockRestaurant(id);
    }

    @Override
    public Restaurant insert(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant update(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public void delete(int id) {
        restaurantRepository.deleteById(id);
    }

    @Override
    public Collection<Restaurant> gettAll() {
        return (Collection<Restaurant>) restaurantRepository.findAll();
    }
}
