package com.sharefood.ShareFood.service.impl;

import com.sharefood.ShareFood.model.Image;
import com.sharefood.ShareFood.model.Restaurant;
import com.sharefood.ShareFood.repository.ImageRepository;
import com.sharefood.ShareFood.service.base.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public List<Image> findAllByRestaurant(Restaurant restaurant) {
        return imageRepository.findAllByRestaurant(restaurant);
    }

    @Override
    public Image insert(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Image update(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public void delete(int id) {
        imageRepository.deleteById(id);
    }

    @Override
    public Collection<Image> gettAll() {
        return (Collection<Image>) imageRepository.findAll();
    }
}
