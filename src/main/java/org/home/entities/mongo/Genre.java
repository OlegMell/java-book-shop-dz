package org.home.entities.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Document("genres")
public class Genre {
    @Id
    private String id;

    private String name;

    @OneToMany(mappedBy = "genre")
    private List<Book> books;

    public Genre() {
    }

    public Genre(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

