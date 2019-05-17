package com.sharefood.ShareFood.service.base;

import com.sharefood.ShareFood.model.Price;
import com.sharefood.ShareFood.model.Restaurant;

import java.util.List;

public interface PriceService extends Service<Price> {
    List<Price> findWithPage(int page);

    int countAll();

    List<Price> findAllByRestaurant(Restaurant restaurant);

    List<Price> searchWithProvince(int id);

    List<Price> searchWithCategory(int id);
}
