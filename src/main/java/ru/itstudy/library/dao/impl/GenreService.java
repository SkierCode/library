package ru.itstudy.library.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itstudy.library.dao.GenreDao;
import ru.itstudy.library.domain.Genre;
import ru.itstudy.library.spring.repository.GenreRepository;

import java.util.List;

@Service
@Transactional
public class GenreService implements GenreDao {
    @Autowired
    GenreRepository genreRepository;

    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    @Override
    public List<Genre> getAll(Sort sort) {
        return genreRepository.findAll(sort);
    }

    @Override
    public Page<Genre> getAll(int pageNumber, int pageSize, String sortField, Sort.Direction sortDirection) {
        return genreRepository.findAll(PageRequest.of(pageNumber,pageSize, Sort.by(sortDirection, sortField)));
    }

    @Override
    public Genre get(long id) {
        return genreRepository.findById(id).orElse(null);
    }

    @Override
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public void delete(Genre genre) {
        genreRepository.delete(genre);
    }

    @Override
    public List<Genre> search(String... searchString) {
        return genreRepository.findByNameContainingIgnoreCaseOrderByName(searchString[0]);
    }

    @Override
    public Page<Genre> search(int pageNumber, int pageSize, String sortField, Sort.Direction sortDirection, String... searchString) {
        return genreRepository.findByNameContainingIgnoreCaseOrderByName(searchString[0], PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortField)));
    }
}
