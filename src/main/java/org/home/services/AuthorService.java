package org.home.services;

import org.home.dto.AuthorDto;
import org.home.entities.Author;
import org.home.repositories.AuthorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        var authors = this.authorsRepos.findAll();
        if (authors.size() == 0) {
            authors = null;
        }
        return CompletableFuture.completedFuture(authors);
    }
}
