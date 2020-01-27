package ru.itstudy.library.jsfui.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.itstudy.library.dao.GenreDao;
import ru.itstudy.library.domain.Genre;
import ru.itstudy.library.jsfui.model.LazyDataTable;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import java.util.List;

@ManagedBean
@SessionScoped
@Component
@Getter
@Setter
@Log
public class GenreController extends AbstractController<Genre> {
    private int rowCount = 20;
    private int first;

    @Autowired
    private GenreDao genreDao;
    private Genre selectedGenre;
    private LazyDataTable<Genre> lazyModel;
    private Page<Genre> genrePages;

    @PostConstruct
    public void init() {
        lazyModel = new LazyDataTable(this);
    }

    public List<Genre> find (String name) {
        return genreDao.search(name);
    }

    public void save() {
        genreDao.save(selectedGenre);
        RequestContext.getCurrentInstance().execute("PF('dialogGenre').hide");
    }

    @Override
    public Page<Genre> search(int first, int count, String SortField, Sort.Direction sortDirection) {
        return genrePages;
    }

    @Override
    public void addAction() {

    }

    @Override
    public void editAction() {

    }

    @Override
    public void deleteAction() {

    }

    public List<Genre> getAll() {
        return genreDao.getAll(Sort.by(Sort.Direction.ASC, "name"));
    }
}
