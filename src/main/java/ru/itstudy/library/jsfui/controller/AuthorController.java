package ru.itstudy.library.jsfui.controller;

import lombok.Getter;
import lombok.Setter;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.itstudy.library.dao.AuthorDao;
import ru.itstudy.library.domain.Author;
import ru.itstudy.library.jsfui.model.LazyDataTable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.annotation.PostConstruct;
import java.util.List;


@ManagedBean
@SessionScoped
@Component
@Getter
@Setter
public class AuthorController extends AbstractController<Author> {
    // из JSF таблицы обязательно должна быть ссылки на переменные, иначе при использовании постраничности dataTable работает некорректно
    // также - выбранное пользователем значение (кол-во записей на странице) будет сохраняться
    private int rowsCount = 20;
    private int first;

    @Autowired
    private AuthorDao authorDao;

    private Author selectedAuthor;
    private LazyDataModel<Author> lazyModel;
    private Page<Author> authorPages;

    @PostConstruct
    public void init() {
        lazyModel = new LazyDataTable<>(this);
    }

    public void save() {
        authorDao.save(selectedAuthor);
        RequestContext.getCurrentInstance().execute("PF('dialogAuthor').hide()");
    }

    @Override
    public Page<Author> search(int first, int count, String SortField, Sort.Direction sortDirection) {
        return authorPages;
    }

    @Override
    public void addAction() {
        selectedAuthor = new Author();
        showEditDialog();
    }

    @Override
    public void editAction() {

        // выбранный author уже будет записан в переменную selectedAuthor (как только пользователь кликнет на редактирование)
        // он отобразится в диалоговом окне
        showEditDialog();
    }

    @Override
    public void deleteAction() {
        // выбранный author уже будет записан в переменную selectedAuthor (как только пользователь кликнет на удаление)
        authorDao.delete(selectedAuthor);
    }

    private void showEditDialog() {
        // показывает диалоговое окно со значениями selectedAuthor
        RequestContext.getCurrentInstance().execute("PF('dialogAuthor').show()");
    }


    public List<Author> find (String fio) {
        return authorDao.search(fio);
    }
}
