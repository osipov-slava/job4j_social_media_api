package ru.job4j.socialmediaapi.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.job4j.socialmediaapi.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

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

    @Transactional
    @Modifying
    @Query("""
            update User u
            set u.email = :#{#user.email},
             u.password = :#{#user.password},
             u.timezone = :#{#user.timezone}
            where u.id = :#{#user.id}
            """)
    int update(@Param("user") User user);

    @Transactional
    @Modifying
    @Query("delete from User u where u.id=:pId")
    int delete(@Param("pId") Long id);

}
