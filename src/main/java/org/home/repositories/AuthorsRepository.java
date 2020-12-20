package org.home.repositories;

import org.home.entities.Author;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorsRepository extends CrudRepository<Author, Long> {
    List<Author> findAll();
}
