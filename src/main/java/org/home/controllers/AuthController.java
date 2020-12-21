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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

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
                               Model model
    ) {

        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors()
                    .stream()
                    .collect(Collectors
                            .toMap(fieldError ->
                                    fieldError.getField() + "Error", FieldError::getDefaultMessage));

            model.mergeAttributes(errors);
            model.addAttribute("login", userValidDto.getUsername());

            return "auth/registration";
        }

        User findUser = userRepo.findUserByUsername(userValidDto.getUsername());
        if (findUser != null) {
            model.addAttribute("message", "User with this username is exists! Please login");
            return "auth/registration";
        }

        usersService.addNewUser(userValidDto);

        return "redirect:/auth/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable("code") String code,
                           Model model
    ) {

        if (this.usersService.activateUser(code)) {
            model.addAttribute("message", "Success activated");
        } else {
            model.addAttribute("message", "Error activate!");
        }

        return "/auth/login";
    }
}
