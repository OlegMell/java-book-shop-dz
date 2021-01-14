package org.home.controllers;

import com.dropbox.core.DbxException;
import org.home.dto.BookValidationDto;
import org.home.entities.mongo.Author;
import org.home.entities.mongo.Book;
import org.home.entities.mongo.Genre;
import org.home.entities.mongo.User;
import org.home.helpers.ExcelHelper;
import org.home.services.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller()
@RequestMapping({"/", "/books"})
public class BookController {
    private final UsersService usersService;
    private final GenreService genreService;
    private final BooksService booksService;
    private final ValidationService validationService;
    private final AuthorService authorService;
    private final ExcelService excelService;
    private final DropBoxService dropBoxService;


    public BookController(UsersService usersService,
                          GenreService genreService,
                          BooksService booksService,
                          ValidationService validationService,
                          AuthorService authorService,
                          ExcelService excelService,
                          DropBoxService dropBoxService) {
        this.usersService = usersService;
        this.genreService = genreService;
        this.booksService = booksService;
        this.validationService = validationService;
        this.authorService = authorService;
        this.excelService = excelService;
        this.dropBoxService = dropBoxService;
    }


    @GetMapping("/")
    public String getBooks(@AuthenticationPrincipal User user,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "3") int size,
                           Model model) throws ExecutionException, InterruptedException, DbxException {
        CompletableFuture<List<Book>> bookCompletableFuture = this.booksService.getAllBooks(page, size);
        CompletableFuture<List<Genre>> genreCompletableFuture = this.genreService.getAllGenres();
        CompletableFuture<List<Author>> authorCompletableFuture = this.authorService.getAllAuthors();
        CompletableFuture<Long> countCompletableFuture = this.booksService.getBookCount();


        CompletableFuture.allOf(bookCompletableFuture,
                genreCompletableFuture,
                authorCompletableFuture,
                countCompletableFuture).join();

        List<Book> books = bookCompletableFuture.get();
        model.addAttribute("books", books);
        model.addAttribute("genres", genreCompletableFuture.get());
        model.addAttribute("authors", authorCompletableFuture.get());
        Long booksCount = countCompletableFuture.get();
        double pages = Math.ceil(booksCount / size);
        model.addAttribute("pages", pages);

        if (user != null) {
            model.addAttribute("isActive", user.getActivateCode() == null);
            model.addAttribute("isBlocked", user.isBlocked());
        } else {
            model.addAttribute("isActive", true);
        }

        return "index";
    }

    @GetMapping("/add-book")
    public String addBook(Model model) throws ExecutionException, InterruptedException {
        CompletableFuture<List<Author>> listCompletableFuture = this.authorService.getAllAuthors();
        CompletableFuture.allOf(listCompletableFuture).join();
        List<Author> authors = listCompletableFuture.get();
        model.addAttribute("authors", authors);
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
            CompletableFuture<List<Author>> authorCompletableFuture = this.authorService.getAllAuthors();
            CompletableFuture.allOf(authorCompletableFuture).join();
            model.addAttribute("authors", authorCompletableFuture.get());

            return "books/add-book";
        }

        try {
            String imageLink = this.dropBoxService.uploadFile(bookValidDto.getImage().getInputStream(), bookValidDto.getImage().getOriginalFilename());
            bookValidDto.setImageLink(imageLink);
        } catch (IOException e) {
            e.printStackTrace();
        }


        CompletableFuture<Book> objectCompletableFuture = this.booksService.addNewBook(bookValidDto,
                firstnames,
                lastnames,
                user);
        CompletableFuture.allOf(objectCompletableFuture).join();
        objectCompletableFuture.get();
        return "redirect:/";
    }


    @GetMapping("/your-books/{id}")
    public String yourBook(@PathVariable String id, Model model) throws
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
    public String editBook(@PathVariable("id") String id, Model model) throws ExecutionException, InterruptedException {
        this.setBookFormModel(id, model);

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
            this.setBookFormModel(bookValidDto.getId(), model);

            return "books/edit-book";
        }

        CompletableFuture<Book> bookCompFuture = this.booksService.editBook(bookValidDto, firstnames, lastnames, user);
        CompletableFuture.allOf(bookCompFuture).join();
        bookCompFuture.get();

        return "redirect:/";
    }

    @GetMapping("/remove-book/{id}")
    public String removeBook(@PathVariable("id") String id) throws ExecutionException, InterruptedException {
        CompletableFuture<Object> objectCompletableFuture = this.booksService.removeBook(id);
        CompletableFuture.allOf(objectCompletableFuture).join();
        objectCompletableFuture.get();
        return "redirect:/";
    }

    @PostMapping("/add-book-excel")
    public String addBookFromExcel(@RequestParam("file") MultipartFile file) {
        if (ExcelHelper.hasExcelFormat(file)) {
            excelService.save(file);
        }

        return "redirect:/";
    }

    @GetMapping("/download-books")
    public ResponseEntity<Resource> downloadBooks() {
        String filename = "books.xlsx";
        InputStreamResource file = null;

        try {
            file = new InputStreamResource(this.excelService.load());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }


    private void setBookFormModel(String bookId, Model model) throws ExecutionException, InterruptedException {
        CompletableFuture<Book> bookCompletableFuture = this.booksService.getBookById(bookId);
        CompletableFuture<List<Author>> listCompletableFuture = this.authorService.getAllAuthors();
        CompletableFuture.allOf(listCompletableFuture, bookCompletableFuture).join();

        Book book = bookCompletableFuture.get();
        List<Author> authors = listCompletableFuture.get();

        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
    }
}
