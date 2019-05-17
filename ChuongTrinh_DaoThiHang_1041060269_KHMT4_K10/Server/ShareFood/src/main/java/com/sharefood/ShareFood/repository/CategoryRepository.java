package com.sharefood.ShareFood.repository;

import com.sharefood.ShareFood.model.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

    List<Category> findAll();
}
