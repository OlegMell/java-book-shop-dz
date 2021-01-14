package org.home.services;

import org.home.entities.mongo.Book;
import org.home.helpers.ExcelHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class ExcelService {
    private final BooksService booksService;

    public ExcelService(BooksService booksService) {
        this.booksService = booksService;
    }

    public void save(MultipartFile file) {
        try {
            List<Book> books = ExcelHelper.mapExcel(file.getInputStream());
            booksService.addBooks(books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ByteArrayInputStream load() throws ExecutionException, InterruptedException {
        CompletableFuture<List<Book>> completableFutureBooks = this.booksService.getAllBooks(0, 100);
        CompletableFuture.allOf(completableFutureBooks).join();
        List<Book> books = completableFutureBooks.get();
        return ExcelHelper.booksToExcel(books);
    }
}
