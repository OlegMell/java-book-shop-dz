package org.home.services;

import org.home.entities.mongo.Author;
import org.home.repositories.mongo.AuthorsRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AuthorService {
    private final AuthorsRepository authorsRepos;

    public AuthorService(AuthorsRepository authorsRepos) {
        this.authorsRepos = authorsRepos;
    }

    @Async
    public CompletableFuture<List<Author>> getAllAuthors() {
        List<Author> authors = this.authorsRepos.findAll();
        if (authors.size() == 0) {
            authors = null;
        }
        return CompletableFuture.completedFuture(authors);
    }

    @Async
    public CompletableFuture<Author> addAuthor(Author author) {
        return CompletableFuture.completedFuture(this.authorsRepos.save(author));
    }

    @Async
    public CompletableFuture<Author> getById(String id) {
        return CompletableFuture.completedFuture(this.authorsRepos.findById(id).orElse(null));
    }
}
