package org.home.api.v1.controllers;

import org.home.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersApiController {
    private final UsersService usersService;

    public UsersApiController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/block-user")
    public Object blockUser(@RequestParam() String id,
                            @RequestParam() boolean blocked) {
        if (this.usersService.blockUser(Long.parseLong(id), blocked)) {
            return "ok";
        }

        return "error";
    }

    @PostMapping("/block-user")
    public Object blockUser(@RequestBody() Long id) {
        int a = 0;

        return "ok";
    }

}
