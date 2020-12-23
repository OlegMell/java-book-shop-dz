package org.home.services;

import org.home.dto.AuthorDto;
import org.home.entities.Author;
import org.home.repositories.AuthorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorsRepository authorsRepos;

    public AuthorService(AuthorsRepository authorsRepos) {
        this.authorsRepos = authorsRepos;
    }

    public List<Author> getAllAuthors() {
        var authors = this.authorsRepos.findAll();
        if (authors.size() == 0) {
            authors = null;
        }
        return authors;
    }
}
