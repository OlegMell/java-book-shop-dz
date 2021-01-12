package org.home.repositories.mongo;

import org.home.entities.mongo.Author;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AuthorsRepository extends MongoRepository<Author, String> {
    List<Author> findAll();
}
