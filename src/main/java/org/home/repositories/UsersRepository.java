package org.home.repositories;

import org.home.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UsersRepository extends CrudRepository<User, Long> {
    User findUserByUsername(String username);
    User findUserByActivateCode(String code);
    List<User> findAll();

    @Query(value = "SELECT DISTINCT * FROM usrs WHERE unblock_date = DATE(NOW())", nativeQuery = true)
    List<User> getUsersByUnblockDate();
}
