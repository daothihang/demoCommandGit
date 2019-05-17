package com.sharefood.ShareFood.service.base;

import com.sharefood.ShareFood.model.Image;
import com.sharefood.ShareFood.model.Restaurant;

import java.util.List;

public interface ImageService extends Service<Image> {
    List<Image> findAllByRestaurant(Restaurant restaurant);
}
