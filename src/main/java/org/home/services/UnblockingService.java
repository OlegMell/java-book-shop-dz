package org.home.services;

import org.springframework.stereotype.Service;

@Service
public class UnblockingService {
    private final UsersService usersService;

    public UnblockingService(UsersService usersService) {
        this.usersService = usersService;
    }

    public void unblockUsers() {
        this.usersService.unblockUsers();
    }
}
