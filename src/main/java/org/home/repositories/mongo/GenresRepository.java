package org.home.repositories.mongo;

import org.home.entities.mongo.Genre;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GenresRepository extends MongoRepository<Genre, String> {
    Genre findByName(String name);
}

