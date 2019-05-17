package com.sharefood.ShareFood.service.base;

import com.sharefood.ShareFood.model.AbstractModel;

import java.util.Collection;

public interface Service<E extends AbstractModel> {

    E insert(E e);

    E update(E e);

    void delete(int id);

    Collection<E> gettAll();
}
