package org.home.api.v1.controllers;

import org.home.dto.UserUnblockDateDto;
import org.home.services.UsersService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersApiController {
    private final UsersService usersService;

    public UsersApiController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/set-unblock-date")
    public Object setBlockDate(@RequestBody UserUnblockDateDto userUnblockDateDto) {
        if (this.usersService.setUnblockDate(userUnblockDateDto)) {
            return "ok";
        }

        return "error";
    }
}
