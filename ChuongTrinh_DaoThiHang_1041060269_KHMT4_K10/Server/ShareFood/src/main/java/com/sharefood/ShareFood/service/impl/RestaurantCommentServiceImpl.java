package com.sharefood.ShareFood.service.impl;

import com.sharefood.ShareFood.model.Restaurant;
import com.sharefood.ShareFood.model.RestaurantComment;
import com.sharefood.ShareFood.repository.RestaurantCommentRepository;
import com.sharefood.ShareFood.service.base.RestaurantCommentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantCommentServiceImpl implements RestaurantCommentService {

    @Autowired
    private RestaurantCommentRepository restaurantCommentRepository;

    @Override
    public List<RestaurantComment> findAllByRestaurant(Restaurant restaurant) {
        return restaurantCommentRepository.findAllByRestaurant(restaurant);
    }

    @Override
    public RestaurantComment insert(RestaurantComment restaurantComment) {
        return restaurantCommentRepository.save(restaurantComment);
    }

    @Override
    public RestaurantComment update(RestaurantComment restaurantComment) {
        return restaurantCommentRepository.save(restaurantComment);
    }

    @Override
    public void delete(int id) {
        restaurantCommentRepository.deleteById(id);
    }

    @Override
    public Collection<RestaurantComment> gettAll() {
        return (Collection<RestaurantComment>) restaurantCommentRepository.findAll();
    }
}
