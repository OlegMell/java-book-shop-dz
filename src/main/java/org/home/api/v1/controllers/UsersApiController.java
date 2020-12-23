package org.home.api.v1.controllers;

import org.home.dto.UserUnblockDateDto;
import org.home.services.UsersService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/users")
public class UsersApiController {
    private final UsersService usersService;

    public UsersApiController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/set-unblock-date")
    public Object setBlockDate(@RequestBody UserUnblockDateDto userUnblockDateDto) throws ExecutionException, InterruptedException {
        CompletableFuture<Boolean> future = this.usersService.setUnblockDate(userUnblockDateDto);
        CompletableFuture.allOf(future).join();

        if (future.get()) {
            return "ok";
        }

        return "error";
    }
}
