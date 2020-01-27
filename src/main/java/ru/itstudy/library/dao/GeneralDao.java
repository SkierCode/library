package ru.itstudy.library.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface GeneralDao<T> {
    //получение всех зпаписей без постраничности
    List<T> getAll();

    T get(long id);

    T save(T obj);

    void delete(T object);

    //поиск записей с любым количеством параметеров
    List<T> search(String... searchString);

    //получение всех записей с сортировкой результата
    List<T> getAll(Sort sort);

    //получение всех записей с постраничностью
    Page<T> getAll(int pageNumber, int pageSize, String sortField, Sort.Direction sortDirection);

    //поиск записей с постраничностью и любым количеством параметров
    Page<T> search(int pageNumber, int pageSize, String sortField, Sort.Direction sortDirection, String... searchString);
}
