package ru.job4j.socialmediaapi.service;

import ru.job4j.socialmediaapi.dto.UserPostsDto;
import ru.job4j.socialmediaapi.model.File;
import ru.job4j.socialmediaapi.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Post save(Post post, List<File> files);

    Optional<Post> findById(Long id);

    boolean update(Post post, List<File> files);

    void updateCascade(Post post, List<File> files);

    boolean deleteById(Long id);

    void delete(Post post);

    List<Post> findAll();

    List<UserPostsDto> findAllByUsersId(List<Long> userIds);

}
