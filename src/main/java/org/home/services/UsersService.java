package org.home.services;

import org.home.dto.UserValidationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.home.entities.Role;
import org.home.entities.User;
import org.home.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
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

    public User findByActivationCode(String code) {
        return this.usersRepos.findUserByActivateCode(code);
    }

    public void addNewUser(UserValidationDto userValidDto) {
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
    }

    public boolean activateUser(String code) {
        User user = usersRepos.findUserByActivateCode(code);

        if (user == null) return false;

        user.setActivateCode(null);
        usersRepos.save(user);

        return true;
    }
}
