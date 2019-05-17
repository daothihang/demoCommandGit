package com.sharefood.ShareFood.service.base;

import com.sharefood.ShareFood.model.Category;

import java.util.List;

public interface CategoryService extends Service<Category> {
    List<Category> findAll();
}
