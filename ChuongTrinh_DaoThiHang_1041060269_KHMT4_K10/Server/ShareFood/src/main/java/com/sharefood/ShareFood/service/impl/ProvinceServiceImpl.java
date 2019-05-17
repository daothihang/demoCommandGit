package com.sharefood.ShareFood.service.impl;

import com.sharefood.ShareFood.model.Province;
import com.sharefood.ShareFood.repository.ProvinceRepository;
import com.sharefood.ShareFood.service.base.ProvinceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class ProvinceServiceImpl implements ProvinceService {

    @Autowired
    private ProvinceRepository provinceRepository;


    @Override
    public List<Province> findAll() {
        return provinceRepository.findAll();
    }

    @Override
    public Province insert(Province province) {
        return provinceRepository.save(province);
    }

    @Override
    public Province update(Province province) {
        return provinceRepository.save(province);
    }

    @Override
    public void delete(int id) {
        provinceRepository.deleteById(id);
    }

    @Override
    public Collection<Province> gettAll() {
        return provinceRepository.findAll();
    }
}
