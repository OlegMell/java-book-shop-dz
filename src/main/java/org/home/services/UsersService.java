package org.home.services;

import org.hibernate.internal.build.AllowSysOut;
import org.home.dto.UserUnblockDateDto;
import org.home.dto.UserValidationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.home.entities.Role;
import org.home.entities.User;
import org.home.repositories.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Cacheable("users")
public class UsersService implements UserDetailsService {
    private final UsersRepository usersRepos;
    private final MailSenderService mailSenderService;
    private final PasswordEncoder passEncoder;

    @Value("${current.host}")
    private String currentHost;

    public UsersService(UsersRepository usersRepos,
                        MailSenderService mailSenderService,
                        @Lazy PasswordEncoder passEncoder
    ) {
        this.usersRepos = usersRepos;
        this.mailSenderService = mailSenderService;
        this.passEncoder = passEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return usersRepos.findUserByUsername(s);
    }

    @Async
    public CompletableFuture<User> getUserByUsername(String username) {
        return CompletableFuture.completedFuture(this.usersRepos.findUserByUsername(username));
    }

    @Async
    public CompletableFuture<User> findByActivationCode(String code) {
        return CompletableFuture.completedFuture(this.usersRepos.findUserByActivateCode(code));
    }

    @Async
    public CompletableFuture<Object> addNewUser(UserValidationDto userValidDto) {
        User user = new User();

        user.setUsername(userValidDto.getUsername());
        user.setPassword(passEncoder.encode(userValidDto.getPassword()));
        user.setEmail(userValidDto.getEmail());

        user.setActivateCode(UUID.randomUUID().toString());
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));

        usersRepos.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String
                    .format("%s, Welcome to Book Shop! Please click to this link for active your account! %s/auth/activate/%s",
                            user.getUsername(),
                            currentHost,
                            user.getActivateCode());

            mailSenderService.send(user.getEmail(), "Book Shop Activation Account", message);
        }

        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Boolean> activateUser(String code) {
        User user = usersRepos.findUserByActivateCode(code);

        if (user == null) return CompletableFuture.completedFuture(Boolean.FALSE);

        user.setActivateCode(null);
        usersRepos.save(user);

        return CompletableFuture.completedFuture(Boolean.TRUE);
    }

    @Async
    public CompletableFuture<User> getUserById(Long id) {
        return CompletableFuture.completedFuture(this.usersRepos.findById(id).orElse(null));
    }

    @Async
    public CompletableFuture<List<User>> getAllUsers() {
        var users = this.usersRepos.findAll();
        return CompletableFuture.completedFuture(users);
    }

    @Async
    public CompletableFuture<Boolean> setUnblockDate(UserUnblockDateDto userUnblockDateDto) {
        User user = this.usersRepos.findById(userUnblockDateDto.getId()).orElse(null);

        if (user == null) return CompletableFuture.completedFuture(Boolean.FALSE);

        user.setBlocked(userUnblockDateDto.isBlocked());
        user.setUnblockDate(userUnblockDateDto.getDate());
        this.usersRepos.save(user);

        return CompletableFuture.completedFuture(Boolean.TRUE);
    }

    @Async
    public CompletableFuture<Object> unblockUsers() {
        List<User> users = this.usersRepos.getUsersByUnblockDate();
        users.forEach(user -> {
            user.setBlocked(false);
            user.setUnblockDate(null);
            this.usersRepos.save(user);
        });

        return CompletableFuture.completedFuture(null);
    }
}
