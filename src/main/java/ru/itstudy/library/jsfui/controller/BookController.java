package ru.itstudy.library.jsfui.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.itstudy.library.dao.BookDao;
import ru.itstudy.library.dao.GenreDao;
import ru.itstudy.library.domain.Book;
import ru.itstudy.library.jsfui.enums.SearchType;
import ru.itstudy.library.jsfui.model.LazyDataTable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.ResourceBundle;

@ManagedBean
@SessionScoped
@Component
@Getter
@Setter
@Log
public class BookController extends AbstractController<Book> {

    public static final int DEFAULT_PAGE_SIZE = 20; //сколько книг отображать на странице
    public static final int TOP_BOOKS_LIMIT = 5;
    // из JSF таблицы обязательно должна быть ссылки на переменные, иначе при использовании постраничности dataGrid работает некорректно (не отрабатывает bean)
    // также - выбранное пользователем значение (кол-во записей на странице) будет сохраняться
    private int rowsCount = DEFAULT_PAGE_SIZE;


    SearchType searchType; // запоминает последний выбранный элемент

    @Autowired
    private BookDao bookDao; // будет автоматически подставлен BookService, т.к. Spring контейнер по-умолчанию ищет бин-реализацию по типу
    @Autowired
    private GenreDao genreDao;

    private Book selectedBook; // ссылка на текущую книгу (которую редактируют, хотят удалять и пр.) - т.е. над какой книгой в данный момент производим действие
    private LazyDataTable<Book> lazyModel; // класс утилита, который помогает выводить данные постранично (работает в паре с компонентами на странице JSF)

    private byte[] uploadedImage; // сюда будет сохраняться загруженная пользователем новая обложка (при редактировании или при добавлении книги)
    private byte[] uploadedContent; // сюда будет сохраняться загруженный пользователем PDF контент (при редактировании или при добавлении книги)


    private Page<Book> bookPages; // хранит список найденных книг
    private List<Book> topBooks;// хранит полученные ТОП книги (может использоваться наприемр для получения изображений книги)

    private String searchText; // текст поиска
    private long selectedGenreId; // хранит выбранный жанр (при поиске книг по жанру)

    @PostConstruct
    public void init() {
        lazyModel = new LazyDataTable<>(this);
    }

    public void save() {
        if (uploadedImage != null) {
            selectedBook.setImage(uploadedImage);
        }
        if (uploadedContent != null) {
            selectedBook.setContent(uploadedContent);
        }

        bookDao.save(selectedBook);
        RequestContext.getCurrentInstance().execute("PF('dialogEditBook').hide()"); // вызов JS из Java кода
    }

    @Override
    public Page<Book> search(int pageNumber, int pageSize, String sortField, Sort.Direction sortDirection) {
        if (sortField == null) {
            sortField = "name";
        }

        if (searchType == null) {
            bookPages = bookDao.getAll(pageNumber, pageSize, sortField, sortDirection);
        } else {
            switch (searchType) {
                case SEARCH_GENRE:
                    bookPages = bookDao.findByGenre(pageNumber, pageSize, sortField, sortDirection, selectedGenreId);
                    break;
                case SEARCH_TEXT:
                    bookPages = bookDao.search(pageNumber, pageSize, sortField, sortDirection, searchText);
                    break;
                case ALL:
                    bookPages = bookDao.getAll(pageNumber, pageSize, sortField, sortDirection);
                    break;
            }
        }

        return bookPages;
    }

    @Override
    public void addAction() {

    }

    @Override
    public void editAction() {
        uploadedImage = selectedBook.getImage();

        // выбранный book уже будет записан в переменную selectedBook (как только пользователь кликнет на редактирование)
        // книга отобразится в диалоговом окне
        RequestContext.getCurrentInstance().execute("PF('dialogEditBook').show()");
    }

    @Override
    public void deleteAction() {

    }

    // при закрытии диалогового окна - очищать загруженный контент из переменной
    public void onCloseDialog(CloseEvent event) {
        uploadedContent = null;
    }

    public List<Book> getTopBooks() {
        topBooks = bookDao.findTopBooks(TOP_BOOKS_LIMIT);
        return topBooks;
    }

    public void showBookByGenre(long selectedGenreId) {
        searchType = SearchType.SEARCH_GENRE;
        this.selectedGenreId = selectedGenreId;
    }


    public String getSearchMessage() {
        ResourceBundle bundle = ResourceBundle.getBundle("library", FacesContext.
                getCurrentInstance().getViewRoot().getLocale());

        String message = null;

        if (searchType == null) {
            return null;
        }
        switch (searchType) {
            case SEARCH_GENRE:
                message = bundle.getString("genre") + ": '" + genreDao.get(selectedGenreId) + "'";
                break;
            case SEARCH_TEXT:
                if (searchText == null || searchText.trim().length() == 0) {
                    return null;
                }
                message = bundle.getString("search") + ": '" + searchText + "'";
                break;
        }
        return message;
    }

    public byte[] getContent(long id) {
        if (uploadedContent != null) {
            return uploadedContent;
        } else {
            return bookDao.getContent(id);
        }
    }

    // при загрузке обложки - она будет сохраняться в переменную uploadedImage
    public void uploadImage(FileUploadEvent event) {
        if (event.getFile() != null) {
            uploadedImage = event.getFile().getContents();
        }
    }

    // при загрузке PDF контента - он будет сохраняться в переменную uploadedContent
    public void uploadContent(FileUploadEvent event) {
        if (event.getFile() != null) {
            uploadedContent = event.getFile().getContents();
        }
    }

    public void searchAction() {
        searchType = SearchType.SEARCH_TEXT;
    }

    public void showAll() {
        searchType = SearchType.ALL;
    }

    public void updateViewCount(long viewCount, long id){
        bookDao.updateViewCount(viewCount+1, id);
    }
}
