package ru.job4j.socialmediaapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmediaapi.dto.PostDto;
import ru.job4j.socialmediaapi.dto.UserPostsDto;
import ru.job4j.socialmediaapi.service.PostService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostsControllerTest {

    PostsController postsController;

    PostService postService;

    @BeforeEach
    public void initComponents() {
        postService = mock(PostService.class);
        postsController = new PostsController(postService);
    }

    @Test
    public void whenPassUserIdsThenPostByUserDtos() {
        UserPostsDto userPostsDto1 = new UserPostsDto(1L, "John", List.of(
                new PostDto(1L, "title1", null, null, true, "John", "Kate"),
                new PostDto(2L, "title2", null, null, true, "John", "Kate")));
        UserPostsDto userPostsDto2 = new UserPostsDto(2L, "Kate", List.of(
                new PostDto(3L, "title3", null, null, true, "Kate", "John"),
                new PostDto(4L, "title4", null, null, true, "Kate", "John")));
        var posts = List.of(userPostsDto1, userPostsDto2);
        when(postService.findAllByUsersId(anyList())).thenReturn(posts);

        var responseBody = postsController.findAllByUsersId(List.of(1L, 2L));

        assertThat(responseBody).usingRecursiveComparison().isEqualTo(posts);
    }

}
