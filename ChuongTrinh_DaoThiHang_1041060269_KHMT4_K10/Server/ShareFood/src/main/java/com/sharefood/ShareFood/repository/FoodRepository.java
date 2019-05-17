package com.sharefood.ShareFood.repository;

import com.sharefood.ShareFood.model.Food;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FoodRepository extends CrudRepository<Food, Integer> {

    List<Food> findAll();

    @Query(value="SELECT * FROM tbl_foods ORDER BY id_food DESC LIMIT 5 OFFSET ?1", nativeQuery = true)
    List<Food> findWithPage(int offset);

    @Query(value="SELECT count(*) as count FROM tbl_foods", nativeQuery = true)
    int countAll();

    Food findFoodById(int id);

}
