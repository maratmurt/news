package ru.skillbox.news.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @OneToMany(mappedBy = "article")
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "comment_count")
    private Long commentCount;

}
