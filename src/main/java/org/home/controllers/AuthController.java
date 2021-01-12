package org.home.controllers;

import org.home.dto.CaptchaResponseDto;
import org.home.dto.UserValidationDto;
import org.home.entities.mongo.User;
import org.home.services.GoogleReCaptchaService;
import org.home.services.UsersService;
import org.home.services.ValidationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UsersService usersService;
    private final ValidationService validationService;
    private final GoogleReCaptchaService googleReCaptchaService;

    public AuthController(UsersService usersService,
                          ValidationService validationService,
                          GoogleReCaptchaService googleReCaptchaService
    ) {
        this.usersService = usersService;
        this.validationService = validationService;
        this.googleReCaptchaService = googleReCaptchaService;
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
                               Model model,
                               @RequestParam("g-recaptcha-response") String captchaResp
    ) throws ExecutionException, InterruptedException {
        CaptchaResponseDto res = this.googleReCaptchaService.checkCaptcha(captchaResp);
        if (!res.isSuccess()) {
            model.addAttribute("captchaErrorCodes", res.getErrorCodes());

            return "auth/registration";
        }

        if (result.hasErrors()) {
            Map<String, String> errors = this.validationService.getErrors(result);
            model.mergeAttributes(errors);
            model.addAttribute("login", userValidDto.getUsername());

            return "auth/registration";
        }

        CompletableFuture<User> compFutureUser = this.usersService.getUserByUsername(userValidDto.getUsername());
        CompletableFuture.allOf(compFutureUser).join();
        User findUser = compFutureUser.get();

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
    ) throws ExecutionException, InterruptedException {
        CompletableFuture<Boolean> compFutureResult = this.usersService.activateUser(code);
        CompletableFuture.allOf(compFutureResult).join();

        if (compFutureResult.get()) {
            model.addAttribute("message", "Success activated");
        } else {
            model.addAttribute("message", "Error activate!");
        }

        return "/active-account";
    }
}
