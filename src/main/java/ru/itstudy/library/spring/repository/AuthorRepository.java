package ru.itstudy.library.spring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itstudy.library.domain.Author;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByFioContainingIgnoreCaseOrderByFio(String fio);

    //page содержит резуьтаты выполнения запроса и служебные данные для постраничности
    Page<Author> findByFioContainingIgnoreCaseOrderByFio(String fio, Pageable pageable);// Pageable - параметры для постраничности

}
