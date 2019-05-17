package com.sharefood.ShareFood.repository;

import com.sharefood.ShareFood.model.Image;
import com.sharefood.ShareFood.model.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ImageRepository extends CrudRepository<Image, Integer> {

    List<Image> findAllByRestaurant(Restaurant restaurant);

}
