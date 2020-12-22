package org.home.controllers;

import org.home.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping({"/users"})
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/")
    public String users(Model model) {
        model.addAttribute("users", this.usersService.getAllUsers());
        model.addAttribute("now", LocalDateTime.now());

        return "/users/index";
    }
}
