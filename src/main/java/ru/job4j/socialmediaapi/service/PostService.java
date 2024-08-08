package ru.job4j.socialmediaapi.service;

import ru.job4j.socialmediaapi.model.File;
import ru.job4j.socialmediaapi.model.Post;

import java.util.List;

public interface PostService {

    Post create(Post post, List<File> files);

    void update(Post post, List<File> files);

    void delete(Post post);

}
