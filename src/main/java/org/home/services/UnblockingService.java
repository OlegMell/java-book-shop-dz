package org.home.services;

import org.home.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
