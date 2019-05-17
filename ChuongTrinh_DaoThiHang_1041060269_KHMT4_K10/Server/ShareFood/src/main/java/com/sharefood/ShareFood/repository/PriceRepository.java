package com.sharefood.ShareFood.repository;

import com.sharefood.ShareFood.model.Price;
import com.sharefood.ShareFood.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PriceRepository extends CrudRepository<Price, Integer> {

    @Query(value="SELECT * FROM tbl_prices ORDER BY id_price DESC LIMIT 5 OFFSET ?1", nativeQuery = true)
    List<Price> findWithPage(int offset);

    @Query(value="SELECT count(*) as count FROM tbl_prices", nativeQuery = true)
    int countAll();

    List<Price> findAllByRestaurant(Restaurant restaurant);

    @Query(value = "SELECT tbl_prices.id_price, tbl_prices.price, tbl_prices.id_food, tbl_prices.id_restaurant FROM tbl_restaurants INNER JOIN tbl_prices ON tbl_restaurants.id_restaurant = tbl_prices.id_restaurant INNER JOIN tbl_provinces ON tbl_restaurants.id_province = tbl_provinces.id_province WHERE tbl_restaurants.id_province = ?1", nativeQuery = true)
    List<Price> searchWithProvince(int id);

    @Query(value = "SELECT tbl_prices.id_price, tbl_prices.price, tbl_prices.id_food, tbl_prices.id_restaurant from tbl_foods INNER JOIN tbl_prices ON tbl_foods.id_food = tbl_prices.id_food INNER JOIN tbl_categories ON tbl_foods.id_category = tbl_categories.id_category WHERE tbl_foods.id_category = ?1", nativeQuery = true)
    List<Price> searchWithCategory(int id);

}
