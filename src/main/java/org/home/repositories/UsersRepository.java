package org.home.repositories;

import org.home.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<User, Long> {
    User findUserByUsername(String username);
    User findUserByActivateCode(String code);
}
