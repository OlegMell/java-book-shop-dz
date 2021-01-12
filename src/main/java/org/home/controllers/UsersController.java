package org.home.controllers;

import org.home.entities.mongo.User;
import org.home.services.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping({"/users"})
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/")
    public String users(Model model) throws ExecutionException, InterruptedException {
        CompletableFuture<List<User>> compUsers = this.usersService.getAllUsers();
        CompletableFuture.allOf(compUsers).join();
        List<User> users = compUsers.get();
        model.addAttribute("users", users);
        model.addAttribute("now", LocalDateTime.now());

        return "/users/index";
    }
}
