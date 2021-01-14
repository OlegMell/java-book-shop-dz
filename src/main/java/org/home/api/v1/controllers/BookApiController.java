package org.home.api.v1.controllers;

import org.home.dto.BookDto;
import org.home.entities.mongo.Book;
import org.home.entities.mongo.Genre;
import org.home.helpers.ExcelHelper;
import org.home.repositories.mongo.AuthorsRepository;
import org.home.repositories.mongo.GenresRepository;
import org.home.repositories.mongo.BooksRepository;
import org.home.services.ExcelService;
import org.home.utils.ObjectMapperUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookApiController {
    private final AuthorsRepository authorsRepos;
    private final GenresRepository genresRepos;
    private final BooksRepository booksRepos;

    public BookApiController(AuthorsRepository authorsRepos,
                             GenresRepository genresRepos,
                             BooksRepository booksRepos) {
        this.authorsRepos = authorsRepos;
        this.genresRepos = genresRepos;
        this.booksRepos = booksRepos;
    }

    @GetMapping("/filter")
    public List<BookDto> filterBook(@RequestParam(required = false) String authorId,
                                    @RequestParam(required = false) String genreId,
                                    @RequestParam(required = false) String price) {
        Pageable paging = PageRequest.of(0, 2);

        var bookPage = booksRepos.findAll(paging);
        var books= bookPage.getContent();

        if (!authorId.equalsIgnoreCase("all")) {
            books = books.stream().filter(book ->
                    book.getAuthors()
                            .contains(authorsRepos.findById(authorId).orElse(null)))
                    .collect(Collectors.toList());
        }

        if (!genreId.equalsIgnoreCase("all")) {
            Genre srchGenre = genresRepos.findById(genreId).orElse(null);
            if (srchGenre == null) {
                return null;
            }

            books = books
                    .stream()
                    .filter(book -> book.getGenre().getId().equals(srchGenre.getId())).collect(Collectors.toList());
        }

        if (!StringUtils.isEmpty(price)) {
            books = books.stream()
                    .filter(book -> book.getPrice().equals(Float.parseFloat(price)))
                    .collect(Collectors.toList());
        }

        return convertToBooksDto(books);
    }


    private List<BookDto> convertToBooksDto(Collection<Book> books) {
        return ObjectMapperUtils.mapAll(books, BookDto.class);
    }
}
