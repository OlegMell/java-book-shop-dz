package org.home.services;

import org.home.dto.BookValidationDto;
import org.home.entities.Author;
import org.home.entities.Book;
import org.home.entities.Genre;
import org.home.entities.User;
import org.home.repositories.AuthorsRepository;
import org.home.repositories.BooksRepository;
import org.home.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BooksService {
    private final BooksRepository booksRepos;
    private final GenreRepository genreRepos;
    private final AuthorsRepository authorsRepos;

    private final GenreService genreService;

    public BooksService(BooksRepository booksRepos,
                        GenreRepository genreRepos,
                        AuthorsRepository authorsRepos,
                        GenreService genreService
    ) {
        this.booksRepos = booksRepos;
        this.genreRepos = genreRepos;
        this.authorsRepos = authorsRepos;
        this.genreService = genreService;
    }

    public void addNewBook(BookValidationDto bookValidDto,
                           List<String> firstnames,
                           List<String> lastnames,
                           User user
    ) {
        Genre _findGenre = this.getBookGenre(bookValidDto.getGenre());

        if (_findGenre == null)
            _findGenre = this.genreRepos.save(new Genre(bookValidDto.getGenre()));

        List<Author> _authors = new ArrayList<>();
        if (bookValidDto.getAuthors() == null) {
            var authorsMap = zipToMap(firstnames, lastnames);

            for (Map.Entry<String, String> pair :
                    authorsMap.entrySet()) {
                Author author = authorsRepos.save(new Author(pair.getKey(), pair.getValue()));
                _authors.add(author);
            }
        } else {
            bookValidDto.getAuthors().forEach(author ->
                    authorsRepos.findById(author).ifPresent(_authors::add));
        }

        this.setBookFields(null,
                bookValidDto,
                _findGenre,
                _authors,
                user);
    }

    public void editBook(BookValidationDto bookValidDto,
                         List<String> firstnames,
                         List<String> lastnames,
                         User user
    ) {
        Book book = booksRepos.findById(bookValidDto.getId()).orElse(null);
        Genre _findGenre = this.getBookGenre(bookValidDto.getGenre());

        if (_findGenre == null)
            _findGenre = this.genreRepos.save(new Genre(bookValidDto.getGenre()));


        List<Author> _authors = new ArrayList<>();
        if (bookValidDto.getAuthors() == null) {
            var authorsMap = zipToMap(firstnames, lastnames);

            for (Map.Entry<String, String> pair :
                    authorsMap.entrySet()) {
                Author author = authorsRepos.save(new Author(pair.getKey(), pair.getValue()));
                _authors.add(author);
            }

        } else {
            bookValidDto.getAuthors().forEach(author ->
                    authorsRepos.findById(author).ifPresent(_authors::add));

        }

        this.setBookFields(book,
                bookValidDto,
                _findGenre,
                _authors,
                user);
    }

    private Genre getBookGenre(String genre) {
        return this.genreService.findGenreByName(genre);
    }

    private void setBookFields(Book _book,
                              BookValidationDto bookValidDto,
                              Genre genre,
                              List<Author> authors,
                              User user) {
        if (_book == null) {
            _book = booksRepos.save(new Book(bookValidDto.getTitle(),
                    bookValidDto.getDate(),
                    bookValidDto.getPrice()));
        } else {
            _book.setTitle(bookValidDto.getTitle());
            _book.setDate(bookValidDto.getDate());
            _book.setPrice(Float.parseFloat(bookValidDto.getPrice()));
        }

        _book.setGenre(genre);
        _book.getAuthors().addAll(authors);
        _book.setUser(user);

        booksRepos.save(_book);
    }

    public void removeBook(Long id) {
        booksRepos.deleteById(id);
    }

    public Book getBookById(Long id) {
        return this.booksRepos.findById(id).orElse(null);
    }


    public List<Book> getAllBooks() {
        var books = this.booksRepos.findAll();
        if (books.size() == 0) {
            books = null;
        }
        return books;
    }


    private <K, V> Map<K, V> zipToMap(List<K> keys, List<V> values) {
        return IntStream.range(0, keys.size()).boxed()
                .collect(Collectors.toMap(keys::get, values::get));
    }
}
