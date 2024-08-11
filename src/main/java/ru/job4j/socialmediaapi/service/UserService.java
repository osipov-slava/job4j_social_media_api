package ru.job4j.socialmediaapi.service;

import ru.job4j.socialmediaapi.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    boolean update(User user);

    boolean deleteById(Long id);

}
