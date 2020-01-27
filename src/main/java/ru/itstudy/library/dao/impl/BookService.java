package ru.itstudy.library.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itstudy.library.dao.BookDao;
import ru.itstudy.library.domain.Book;
import ru.itstudy.library.spring.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService implements BookDao {

    @Autowired
    BookRepository bookRepository;

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> getAll(Sort sort) {
        return bookRepository.findAll(sort);
    }

    @Override
    public Page<Book> getAll(int pageNumber, int pageSize, String sortField, Sort.Direction sortDirection) {
        return bookRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortField)));
    }

    @Override
    public List<Book> search(String... searchString) {
        return null;
    }

    @Override
    public Page<Book> search(int pageNumber, int pageSize, String sortField, Sort.Direction sortDirection, String... searchString) {
        final Page<Book> bookPage = bookRepository.findByNameContainingIgnoreCaseOrAuthorFioContainingIgnoreCaseOrderByName(searchString[0], searchString[0],
                PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortField)));
        return bookPage;
    }

    @Override
    public Book save(Book book) {
        bookRepository.save(book);
        if (book.getContent() != null) {
            bookRepository.updateContent(book.getContent(), book.getId());
        }
        return book;
    }

    @Override
    public Book get(long id) {
        Optional<Book> bookmark = bookRepository.findById(id);
        return bookmark.orElse(null);
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    @Override
    public List<Book> findTopBooks(int limit) {
        return bookRepository.findTopBook(PageRequest.of(0, limit, Sort.by(Sort.Direction.ASC, "viewCount")));
    }

    @Override
    public byte[] getContent(long id) {
        return bookRepository.getContent(id);
    }

    @Override
    public Page<Book> findByGenre(int pageNumber, int pageSize, String sortField, Sort.Direction sortDirection, long genreId) {
        return bookRepository.findByGenre(genreId, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, sortField)));
    }

    @Override
    public void updateViewCount(long viewCount, long id) {
        bookRepository.updateViewCount(viewCount, id);
    }

    @Override
    public void updateRating(long totalRating, long totalViewCount, int avgRating, long id) {
        bookRepository.updateVRating(totalRating, totalViewCount, avgRating, id);
    }
}
