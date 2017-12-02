package com.ka.noder.dao;

import com.ka.noder.model.Model;

import java.util.List;

public interface BasicDao<T extends Model> {

    List<T> getAll(String namedQuery, Class<T> tClass);

    T getById(int id, Class<T> tClass);

    void save(T model) throws Exception;

    void update(int id, T model);

    void remove(int id, Class<T> tClass);
}
