package org.home.services;

import org.home.entities.Author;
import org.home.entities.Book;
import org.home.entities.Genre;
import org.home.entities.User;
import org.home.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BooksService {
    private final BooksRepository booksRepos;

    public BooksService(BooksRepository booksRepos) {
        this.booksRepos = booksRepos;
    }

    public void setBookFields(Book _book,
                              String title,
                              String price,
                              Genre genre,
                              List<Author> authors,
                              LocalDate date,
                              User user) {
        if (_book == null) {
            _book = booksRepos.save(new Book(title, date, price));
        } else {
            _book.setTitle(title);
            _book.setDate(date);
            _book.setPrice(Float.parseFloat(price));
        }

        _book.setGenre(genre);
        _book.getAuthors().addAll(authors);
        _book.setUser(user);

        booksRepos.save(_book);
    }

}
