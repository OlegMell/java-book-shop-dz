package org.home.repositories.mongo;

import org.home.entities.mongo.User;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UsersRepository extends MongoRepository<User, String> {
    User findUserByUsername(String username);
    User findUserByActivateCode(String code);
    List<User> findAll();

//    @Query(value = "SELECT DISTINCT * FROM usrs WHERE unblock_date = DATE(NOW())", nativeQuery = true)
    List<User> getUsersByUnblockDate();
}
