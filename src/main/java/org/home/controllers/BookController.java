package org.home.controllers;

import org.apache.tomcat.jni.Local;
import org.home.dto.AddBookDto;
import org.home.dto.AuthorDto;
import org.home.dto.BookDto;
import org.home.entities.Author;
import org.home.entities.Book;
import org.home.entities.Genre;
import org.home.entities.User;
import org.home.repositories.AuthorsRepository;
import org.home.repositories.BooksRepository;
import org.home.repositories.GenreRepository;
import org.home.repositories.UsersRepository;
import org.home.services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller()
@RequestMapping({"/", "/books"})
public class BookController {
    private final BooksRepository booksRepos;
    private final AuthorsRepository authorsRepos;
    private final UsersRepository usersRepos;
    private final GenreRepository genresRepos;
    private final BooksService booksService;

    public BookController(BooksRepository booksRepos,
                          AuthorsRepository authorsRepos,
                          UsersRepository usersRepos,
                          GenreRepository genresRepos, BooksService booksService) {
        this.booksRepos = booksRepos;
        this.authorsRepos = authorsRepos;
        this.usersRepos = usersRepos;
        this.genresRepos = genresRepos;
        this.booksService = booksService;
    }


    @GetMapping("/")
    public String getBooks(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("books", booksRepos.findAll());
        model.addAttribute("genres", genresRepos.findAll());
        model.addAttribute("authors", authorsRepos.findAll());

        if (user != null) {
            model.addAttribute("isActive", user.getActivateCode() == null);
        } else {
            model.addAttribute("isActive", true);
        }

        return "index";
    }

    @GetMapping("/add-book")
    public String addBook(Model model) {
        model.addAttribute("authors", authorsRepos.findAll());
        return "books/add-book";
    }

    @PostMapping("/add-book")
    public String addBook(String title,
                          String genre,
                          String price,
                          @RequestParam("id") User user,
                          @RequestParam(value = "authors", required = false) List<Long> authors,
                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                          @RequestParam(value = "firstname", required = false) List<String> firstnames,
                          @RequestParam(value = "lastname", required = false) List<String> lastnames
    ) {

        Genre _findGenre = genresRepos.findByName(genre);

        if (_findGenre == null)
            _findGenre = genresRepos.save(new Genre(genre));


        List<Author> _authors = new ArrayList<>();
        if (authors == null) {
            var authorsMap = zipToMap(firstnames, lastnames);

            for (Map.Entry<String, String> pair :
                    authorsMap.entrySet()) {
                Author author = authorsRepos.save(new Author(pair.getKey(), pair.getValue()));
                _authors.add(author);
            }
        } else {
            authors.forEach(author -> {
                authorsRepos.findById(author).ifPresent(_authors::add);
            });

        }

        booksService.setBookFields(null, title, price, _findGenre, _authors, date, user);

        return "redirect:/";
    }


    @GetMapping("/your-books/{id}")
    public String yourBook( @PathVariable Long id, Model model) {
        User user = usersRepos.findById(id).orElse(null);
        if (user != null) {
            List<Book> books = user.getBooks();
            model.addAttribute("books", books);
        } else {
            model.addAttribute("warning", "You haven't added any books yet");
        }
        return "books/your-books";
    }

    @GetMapping("/edit-book/{id}")
    public String editBook(@PathVariable("id") Long id, Model model) {
        Book book = booksRepos.findById(id).orElse(null);
        model.addAttribute("book", book);
        model.addAttribute("authors", authorsRepos.findAll());

        return "/books/edit-book";
    }

    @PostMapping("/edit-book")
    public String editBook(String title,
                           String genre,
                           String price,
                           String bookId,
                           @RequestParam("id") User user,
                           @RequestParam(value = "authors", required = false) List<Long> authors,
                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                           @RequestParam(value = "firstname", required = false) List<String> firstnames,
                           @RequestParam(value = "lastname", required = false) List<String> lastnames
    ) {

        Book book = booksRepos.findById(Long.parseLong(bookId)).orElse(null);
        Genre _findGenre = genresRepos.findByName(genre);

        if (_findGenre == null)
            _findGenre = genresRepos.save(new Genre(genre));


        List<Author> _authors = new ArrayList<>();
        if (authors == null) {
            var authorsMap = zipToMap(firstnames, lastnames);

            for (Map.Entry<String, String> pair :
                    authorsMap.entrySet()) {
                Author author = authorsRepos.save(new Author(pair.getKey(), pair.getValue()));
                _authors.add(author);
            }

        } else {
            authors.forEach(author -> {
                authorsRepos.findById(author).ifPresent(_authors::add);
            });

        }

        booksService.setBookFields(book, title, price, _findGenre, _authors, date, user);

        return "redirect:/";
    }

    @GetMapping("/remove-book/{id}")
    public String removeBook(@PathVariable("id") Long id) {
        booksRepos.deleteById(id);

        return "redirect:/";
    }


    private <K, V> Map<K, V> zipToMap(List<K> keys, List<V> values) {
        return IntStream.range(0, keys.size()).boxed()
                .collect(Collectors.toMap(keys::get, values::get));
    }
}
