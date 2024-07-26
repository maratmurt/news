package ru.skillbox.news.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Это поле должно быть заполнено!")
    @Size(min = 2, max = 30, message = "Название должно быть длиной от 2 до 30 символов!")
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Article> articles = new ArrayList<>();

}
