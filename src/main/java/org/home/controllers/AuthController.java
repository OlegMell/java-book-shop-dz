package org.home.controllers;

import org.home.dto.UserValidationDto;
import org.home.entities.Role;
import org.home.entities.User;
import org.home.repositories.UsersRepository;
import org.home.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UsersRepository userRepo;

    private final UsersService usersService;

    public AuthController(UsersRepository userRepo, UsersService usersService) {
        this.userRepo = userRepo;
        this.usersService = usersService;
    }

    @GetMapping("/login")
    public String login() {
        return "/auth/login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registration(@Valid UserValidationDto userValidDto,
                               BindingResult result,
                               Model model) {
        User findUser = userRepo.findUserByUsername(userValidDto.getUsername());
        if (findUser != null) {
            model.addAttribute("message", "User exists");
            return "auth/registration";
        }

        usersService.addNewUser(findUser);

        return "redirect:/auth/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable("code") String code,
                           Model model) {
        boolean isActivated = this.usersService.activateUser(code);
        if (isActivated) {
            model.addAttribute("message", "Success activated");
        } else {
            model.addAttribute("message", "Error activate!");
        }

        return "/auth/login";
    }
}
