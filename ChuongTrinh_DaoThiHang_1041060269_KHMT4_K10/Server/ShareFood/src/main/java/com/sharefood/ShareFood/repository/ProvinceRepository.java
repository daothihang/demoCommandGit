package com.sharefood.ShareFood.repository;

import com.sharefood.ShareFood.model.Province;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProvinceRepository extends CrudRepository<Province, Integer> {

    List<Province> findAll();

}
