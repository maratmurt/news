package ru.skillbox.news.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Это поле должно быть заполнено!")
    @Size(min = 2, max = 30, message = "Имя должно быть длиной от {min} до {max} символов!")
    private String name;

    @NotNull(message = "Это поле должно быть заполнено!")
    @Email(message = "Некорректный адрес электронной почты!")
    private String email;

    @OneToMany(mappedBy = "author")
    private List<Article> articles;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    private String password;

    @Enumerated(EnumType.STRING)
    private Set<RoleType> roles;

}
