package ru.job4j.socialmediaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.job4j.socialmediaapi.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
                select u from User as u
                where u.email = :email and u.password = :password
            """)
    Optional<User> findUser(@Param("email") String email, @Param("password") String password);

    @Query("""
                select r.user from Relationship as r
                where r.partner = ?1 and r.type = 'SUBSCRIBER'
            """)
    List<User> getSubscribersByUser(User user);

    @Query("""
                select r.user from Relationship as r
                where r.partner = ?1 and r.type = 'FRIEND'
            """)
    List<User> getFriendsByUser(User user);

}
