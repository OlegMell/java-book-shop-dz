package org.home.services;

import org.home.entities.mongo.Genre;
import org.home.repositories.mongo.GenresRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class GenreService {
    private final GenresRepository genreRepos;

    public GenreService(GenresRepository genreRepos) {
        this.genreRepos = genreRepos;
    }

    @Async
    public CompletableFuture<Genre> findGenreByName(String genreName) {
        return CompletableFuture.completedFuture(this.genreRepos.findByName(genreName));
    }

    @Async
    public CompletableFuture<List<Genre>> getAllGenres() {
        var genres = (List<Genre>) this.genreRepos.findAll();
        if (genres.size() == 0) {
            genres = null;
        }
        return CompletableFuture.completedFuture(genres);
    }

    @Async
    public CompletableFuture<Genre> addGenre(Genre nGenre) {
         return CompletableFuture.completedFuture(this.genreRepos.save(nGenre));
    }
}
