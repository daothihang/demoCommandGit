package com.sharefood.ShareFood.repository;

import com.sharefood.ShareFood.model.Restaurant;
import com.sharefood.ShareFood.model.RestaurantComment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RestaurantCommentRepository extends CrudRepository<RestaurantComment, Integer> {

    List<RestaurantComment> findAllByRestaurant(Restaurant restaurant);

}
