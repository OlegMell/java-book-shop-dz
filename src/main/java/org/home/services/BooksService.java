package org.home.services;

import org.home.dto.BookValidationDto;
import org.home.entities.mongo.Author;
import org.home.entities.mongo.Book;
import org.home.entities.mongo.Genre;
import org.home.entities.mongo.User;
import org.home.repositories.mongo.BooksRepository;
import org.home.utils.ZipToMap;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class BooksService {
    private final BooksRepository booksRepos;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BooksService(BooksRepository bookRepos,
                        AuthorService authorService,
                        GenreService genreService
    ) {
        this.booksRepos = bookRepos;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @Async
    public void addBooks(List<Book> books) {
        books.forEach(this::addBookFromExcel);
    }

    private void addBookFromExcel(Book book) {
        this.booksRepos.save(book);
    }

    @Async
    public CompletableFuture<Book> addNewBook(BookValidationDto bookValidDto,
                                              List<String> firstnames,
                                              List<String> lastnames,
                                              User user
    ) throws ExecutionException, InterruptedException {
        return CompletableFuture.completedFuture(this.setBook(null,
                bookValidDto,
                firstnames,
                lastnames,
                user));
    }

    @Async
    public CompletableFuture<Book> editBook(BookValidationDto bookValidDto,
                                            List<String> firstnames,
                                            List<String> lastnames,
                                            User user
    ) throws ExecutionException, InterruptedException {
        Book book = this.booksRepos.findById(bookValidDto.getId()).orElse(null);

        return CompletableFuture.completedFuture(this.setBook(book, bookValidDto, firstnames, lastnames, user));
    }

    private Book setBook(Book existBook,
                         BookValidationDto bookValidDto,
                         List<String> firstnames,
                         List<String> lastnames,
                         User user)
            throws ExecutionException, InterruptedException {

        Genre _findGenre = this.getBookGenre(bookValidDto);

        List<Author> _authors = new ArrayList<>();
        if (bookValidDto.getAuthors() == null) {
            var authorsMap = ZipToMap.map(firstnames, lastnames);

            for (Map.Entry<String, String> pair :
                    authorsMap.entrySet()) {
                CompletableFuture<Author> author = this.authorService.addAuthor(new Author(pair.getKey(), pair.getValue()));
                CompletableFuture.allOf(author).join();
                _authors.add(author.get());
            }

        } else {
            bookValidDto.getAuthors().forEach(author -> {
                CompletableFuture<Author> authorCompFuture = this.authorService.getById(author);
                CompletableFuture.allOf(authorCompFuture).join();

                try {
                    _authors.add(authorCompFuture.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

            });
        }

        return this.setBookFields(existBook,
                bookValidDto,
                _findGenre,
                _authors,
                user);
    }

    private Genre getBookGenre(BookValidationDto bookValidDto) throws ExecutionException, InterruptedException {
        CompletableFuture<Genre> compFutureGenre = this.genreService.findGenreByName(bookValidDto.getGenre());
        CompletableFuture.allOf(compFutureGenre).join();
        Genre _findGenre = compFutureGenre.get();

        CompletableFuture<Genre> compGenreFuture;

        if (_findGenre == null) {
            compGenreFuture = this.genreService.addGenre(new Genre(bookValidDto.getGenre()));
            CompletableFuture.allOf(compGenreFuture).join();
            _findGenre = compFutureGenre.get();
        }

        return _findGenre;
    }

    private Book setBookFields(Book _book,
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

        return booksRepos.save(_book);
    }

    @Async
    public CompletableFuture<Object> removeBook(String id) {
        booksRepos.deleteById(id);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Book> getBookById(String id) {
        return CompletableFuture.completedFuture(this.booksRepos.findById(id).orElse(null));
    }

    @Async
    public CompletableFuture<List<Book>> getAllBooks() {
        var books = this.booksRepos.findAll();

        if (books.size() == 0) {
            books = null;
        }

        return CompletableFuture.completedFuture(books);
    }
}
