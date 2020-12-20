package org.home.repositories;


import org.home.entities.Genre;
import org.springframework.data.repository.CrudRepository;

public interface GenreRepository extends CrudRepository<Genre, Long> {
    public Genre findByName(String name);
}
