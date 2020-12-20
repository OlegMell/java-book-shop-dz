package org.home.api.v1.controllers;

import freemarker.template.utility.StringUtil;
import org.home.dto.BookDto;
import org.home.entities.Author;
import org.home.entities.Book;
import org.home.entities.Genre;
import org.home.repositories.AuthorsRepository;
import org.home.repositories.BooksRepository;
import org.home.repositories.GenreRepository;
import org.home.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookApiController {
    private final AuthorsRepository authorsRepos;
    private final GenreRepository genresRepos;
    private final BooksRepository booksRepos;

    public BookApiController(AuthorsRepository authorsRepos,
                             GenreRepository genresRepos,
                             BooksRepository booksRepos) {
        this.authorsRepos = authorsRepos;
        this.genresRepos = genresRepos;
        this.booksRepos = booksRepos;
    }

    @GetMapping("/filter")
    public List<BookDto> filterBook(@RequestParam(required = false) String authorId,
                                    @RequestParam(required = false) String genreId,
                                    @RequestParam(required = false) String price) {

        List<Book> books = booksRepos.findAll();

        if (!authorId.equalsIgnoreCase("all")) {
            books = books.stream().filter(book ->
                    book.getAuthors()
                            .contains(authorsRepos.findById(Long.parseLong(authorId)).orElse(null)))
                    .collect(Collectors.toList());
        }

        if (!genreId.equalsIgnoreCase("all")) {
            Genre srchGenre = genresRepos.findById(Long.parseLong(genreId)).orElse(null);
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
