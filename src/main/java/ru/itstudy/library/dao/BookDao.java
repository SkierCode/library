package ru.itstudy.library.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import ru.itstudy.library.domain.Book;

import java.util.List;

public interface BookDao extends GeneralDao<Book> {
    //поиск топовых книг
    List<Book> findTopBooks(int limit);

    //получение контента по id
    byte[] getContent(long id);

    //постраничный вывод книг определенного жанра
    Page<Book> findByGenre(int pageNumber, int pageSize, String sortField, Sort.Direction sortDirection, long genreId);

    void updateViewCount(long viewCount, long id);

    void updateRating(long totalRating, long totalViewCount, int avgRating, long id);
}
