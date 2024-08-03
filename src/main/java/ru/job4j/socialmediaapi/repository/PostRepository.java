package ru.job4j.socialmediaapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.socialmediaapi.model.Post;
import ru.job4j.socialmediaapi.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> getPostsByFromUser(User user);

    List<Post> getPostsByCreatedBetween(LocalDateTime from, LocalDateTime to);

    Page<Post> getPostsByOrderByCreated(Pageable pageable);

}
