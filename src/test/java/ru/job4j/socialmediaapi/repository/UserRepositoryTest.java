package ru.job4j.socialmediaapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmediaapi.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void whenSaveUserThenFindById() {
        var user = new User();
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        userRepository.save(user);
        var foundUser = userRepository.findById(user.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void whenFindAllThenReturnAllUsers() {
        var user1 = new User();
        user1.setEmail("john.doe@example.com");
        user1.setPassword("password");
        var user2 = new User();
        user2.setEmail("jane.doe@example.com");
        user2.setPassword("password2");
        userRepository.save(user1);
        userRepository.save(user2);
        var users = userRepository.findAll();
        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getEmail).contains("john.doe@example.com", "jane.doe@example.com");
    }

}
