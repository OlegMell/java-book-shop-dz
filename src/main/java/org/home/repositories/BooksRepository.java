package org.home.repositories;

import org.home.entities.Book;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BooksRepository extends CrudRepository<Book, Long> {
    List<Book> findAll();
}
