package org.home.services;

import org.home.entities.Genre;
import org.home.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {
    private final GenreRepository genreRepos;

    public GenreService(GenreRepository genreRepos) {
        this.genreRepos = genreRepos;
    }


    public Genre findGenreByName(String genreName) {
        return this.genreRepos.findByName(genreName);
    }

    public List<Genre> getAllGenres() {
        return (List<Genre>) this.genreRepos.findAll();
    }
}
