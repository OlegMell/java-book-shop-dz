package org.home.entities.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Document("authors")
public class Author {
    @Id
    private String id;

    private String firstName;
    private String lastName;

    public Author(){}

    public Author(String _author) {
        this.firstName = _author;
        this.lastName = " ";
    }

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @ManyToMany(mappedBy = "authors")
    private List<Book> books = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
