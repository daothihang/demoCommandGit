package com.sharefood.ShareFood.service.base;

import com.sharefood.ShareFood.model.Restaurant;
import com.sharefood.ShareFood.model.RestaurantComment;

import java.util.List;

public interface RestaurantCommentService extends Service<RestaurantComment> {
    List<RestaurantComment> findAllByRestaurant(Restaurant restaurant);
}
