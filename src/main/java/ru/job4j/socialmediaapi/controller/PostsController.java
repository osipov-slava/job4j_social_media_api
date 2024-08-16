package ru.job4j.socialmediaapi.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.job4j.socialmediaapi.dto.UserPostsDto;
import ru.job4j.socialmediaapi.model.Post;
import ru.job4j.socialmediaapi.service.PostService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/posts")
public class PostsController {

    private final PostService postService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<Post> findAll() {
        return postService.findAll();
    }

    @PostMapping("/byUsers")
    @ResponseStatus(HttpStatus.OK)
    List<UserPostsDto> findAllByUsersId(@RequestBody List<Long> userIds) {
        return postService.findAllByUsersId(userIds);
    }

}

