package com.sharefood.ShareFood.service.base;

import com.sharefood.ShareFood.model.Province;

import java.util.List;

public interface ProvinceService extends Service<Province> {

    List<Province> findAll();

}
