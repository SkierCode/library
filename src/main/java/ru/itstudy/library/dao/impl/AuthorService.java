package ru.itstudy.library.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itstudy.library.dao.AuthorDao;
import ru.itstudy.library.domain.Author;
import ru.itstudy.library.spring.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthorService implements AuthorDao {

    @Autowired
    AuthorRepository authorRepository;

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    public List<Author> getAll(Sort sort) {
        return authorRepository.findAll(sort);
    }

    @Override
    public Page<Author> getAll(int pageNumber, int pageSize, String sortField, Sort.Direction sortDirection) {
        return authorRepository.findAll(PageRequest.of(pageNumber,pageSize, Sort.by(sortDirection, sortField)));
    }

    @Override
    public Author get(long id) {
        Optional<Author> bookmark = authorRepository.findById(id);
        return bookmark.orElse(null);
    }

    @Override
    public Author save(Author obj) {
        return save(obj);
    }

    @Override
    public void delete(Author author) {
        authorRepository.delete(author);
    }

    @Override
    public List<Author> search(String... searchString) {
        return authorRepository.findByFioContainingIgnoreCaseOrderByFio(searchString[0]);
    }

    @Override
    public Page<Author> search(int pageNumber, int pageSize, String sortField, Sort.Direction sortDirection, String... searchString) {
        return authorRepository.findByFioContainingIgnoreCaseOrderByFio(searchString[0],
                PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, sortField)));
    }
}
