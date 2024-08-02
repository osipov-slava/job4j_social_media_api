package ru.job4j.socialmediaapi.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.socialmediaapi.model.Post;

public interface PostRepository extends ListCrudRepository<Post, Long> {
}
