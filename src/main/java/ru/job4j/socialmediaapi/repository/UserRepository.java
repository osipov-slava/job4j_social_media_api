package ru.job4j.socialmediaapi.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.socialmediaapi.model.User;

public interface UserRepository extends ListCrudRepository<User, Long> {
}
