package com.diachenko.dietblog.service;
/*  diet-blog
    17.02.2025
    @author DiachenkoDanylo
*/

import java.util.List;

public interface SimpleService<T> {
    T save(T t);

    T findById(int id);

    List<T> findAll();

    boolean deleteById(int id);
}
