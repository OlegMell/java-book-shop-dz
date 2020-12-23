package org.home.controllers;

import org.apache.tomcat.jni.Local;
import org.home.dto.AddBookDto;
import org.home.dto.AuthorDto;
import org.home.dto.BookDto;
import org.home.dto.BookValidationDto;
import org.home.entities.Author;
import org.home.entities.Book;
import org.home.entities.Genre;
import org.home.entities.User;
import org.home.repositories.AuthorsRepository;
import org.home.repositories.BooksRepository;
import org.home.repositories.GenreRepository;
import org.home.repositories.UsersRepository;
import org.home.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller()
@RequestMapping({"/", "/books"})
public class BookController {
    private final UsersService usersService;
    private final GenreService genreService;
    private final BooksService booksService;
    private final ValidationService validationService;
    private final AuthorService authorService;

    public BookController(UsersService usersService,
                          GenreService genreService,
                          BooksService booksService,
                          ValidationService validationService,
                          AuthorService authorService) {
        this.usersService = usersService;
        this.genreService = genreService;
        this.booksService = booksService;
        this.validationService = validationService;
        this.authorService = authorService;
    }


    @GetMapping("/")
    public String getBooks(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("books", this.booksService.getAllBooks());
        model.addAttribute("genres", this.genreService.getAllGenres());
        model.addAttribute("authors", this.authorService.getAllAuthors());

        if (user != null) {
            model.addAttribute("isActive", user.getActivateCode() == null);
            model.addAttribute("isBlocked", user.isBlocked());
        } else {
            model.addAttribute("isActive", true);
        }

        return "index";
    }

    @GetMapping("/add-book")
    public String addBook(Model model) {
        model.addAttribute("authors", this.authorService.getAllAuthors());
        return "books/add-book";
    }

    @PostMapping("/add-book")
    public String addBook(@Valid BookValidationDto bookValidDto,
                          BindingResult result,
                          Model model,
                          @RequestParam("id") User user,
                          @RequestParam(value = "firstname", required = false) List<String> firstnames,
                          @RequestParam(value = "lastname", required = false) List<String> lastnames
    ) throws ExecutionException, InterruptedException {

        if (result.hasErrors()) {
            Map<String, String> errors = this.validationService.getErrors(result);
            model.mergeAttributes(errors);
            model.addAttribute("authors", this.authorService.getAllAuthors());

            return "books/add-book";
        }

        CompletableFuture<Object> objectCompletableFuture = this.booksService.addNewBook(bookValidDto, firstnames, lastnames, user);
        CompletableFuture.allOf(objectCompletableFuture).join();
        objectCompletableFuture.get();
        return "redirect:/";
    }


    @GetMapping("/your-books/{id}")
    public String yourBook(@PathVariable Long id, Model model) throws
            ExecutionException,
            InterruptedException {
        CompletableFuture<User> userCompletableFuture = this.usersService.getUserById(id);
        CompletableFuture.allOf(userCompletableFuture).join();
        User user = userCompletableFuture.get();

        if (user != null) {
            List<Book> books = user.getBooks();
            model.addAttribute("books", books);
        } else {
            model.addAttribute("warning", "You haven't added any books yet");
        }

        return "books/your-books";
    }

    @GetMapping("/edit-book/{id}")
    public String editBook(@PathVariable("id") Long id, Model model) throws ExecutionException, InterruptedException {
        CompletableFuture<Book> bookCompletableFuture = this.booksService.getBookById(id);
        CompletableFuture.allOf(bookCompletableFuture).join();
        Book book = bookCompletableFuture.get();
        model.addAttribute("book", book);
        model.addAttribute("authors", this.authorService.getAllAuthors());

        return "/books/edit-book";
    }

    @PostMapping("/edit-book")
    public String editBook(@Valid BookValidationDto bookValidDto,
                           @RequestParam("id") User user,
                           @RequestParam(value = "firstname", required = false)
                                   List<String> firstnames,
                           @RequestParam(value = "lastname", required = false)
                                   List<String> lastnames,
                           BindingResult result,
                           Model model
    ) throws ExecutionException, InterruptedException {

        if (result.hasErrors()) {
            Map<String, String> errors = this.validationService.getErrors(result);
            model.mergeAttributes(errors);
            CompletableFuture<Book> bookCompletableFuture = this.booksService.getBookById(bookValidDto.getId());
            CompletableFuture.allOf(bookCompletableFuture).join();
            Book book = bookCompletableFuture.get();
            model.addAttribute("book", book);
            model.addAttribute("authors", this.authorService.getAllAuthors());

            return "books/edit-book";
        }

        this.booksService.editBook(bookValidDto, firstnames, lastnames, user);

        return "redirect:/";
    }

    @GetMapping("/remove-book/{id}")
    public String removeBook(@PathVariable("id") Long id) {
        this.booksService.removeBook(id);

        return "redirect:/";
    }
}
