package com.sharefood.ShareFood.service.impl;

import com.sharefood.ShareFood.model.Price;
import com.sharefood.ShareFood.model.Restaurant;
import com.sharefood.ShareFood.repository.PriceRepository;
import com.sharefood.ShareFood.service.base.PriceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class PriceServiceImpl implements PriceService {

    @Autowired
    private PriceRepository priceRepository;

    @Override
    public List<Price> findWithPage(int page) {
        int limit = 5;
        int offset = limit*(page - 1);
        return priceRepository.findWithPage(offset);
    }

    @Override
    public int countAll() {
        return priceRepository.countAll();
    }

    @Override
    public List<Price> findAllByRestaurant(Restaurant restaurant) {
        return priceRepository.findAllByRestaurant(restaurant);
    }

    @Override
    public List<Price> searchWithProvince(int id) {
        return priceRepository.searchWithProvince(id);
    }

    @Override
    public List<Price> searchWithCategory(int id) {
        return priceRepository.searchWithCategory(id);
    }

    @Override
    public Price insert(Price price) {
        return priceRepository.save(price);
    }

    @Override
    public Price update(Price price) {
        return priceRepository.save(price);
    }

    @Override
    public void delete(int id) {
        priceRepository.deleteById(id);
    }

    @Override
    public Collection<Price> gettAll() {
        return null;
    }
}
