package ru.itstudy.library.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@DynamicUpdate
@DynamicInsert
@SelectBeforeUpdate
@Getter
@Setter
@Table(catalog = "library")
@EqualsAndHashCode(of = "id")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "book_id")
    private Date bookId;

    private String username;
}
