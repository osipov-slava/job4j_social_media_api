package ru.job4j.socialmediaapi.service;

import ru.job4j.socialmediaapi.dto.request.SignupRequestDTO;
import ru.job4j.socialmediaapi.dto.response.RegisterDTO;
import ru.job4j.socialmediaapi.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    RegisterDTO signUp(SignupRequestDTO signUpRequest);

    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    boolean update(User user);

    boolean deleteById(Long id);

}
