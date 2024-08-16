package ru.job4j.socialmediaapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmediaapi.dto.PostDto;
import ru.job4j.socialmediaapi.dto.UserPostsDto;
import ru.job4j.socialmediaapi.mapstruct.PostMapper;
import ru.job4j.socialmediaapi.mapstruct.UserPostsMapper;
import ru.job4j.socialmediaapi.model.Post;
import ru.job4j.socialmediaapi.model.User;
import ru.job4j.socialmediaapi.repository.FileRepository;
import ru.job4j.socialmediaapi.repository.PostRepository;
import ru.job4j.socialmediaapi.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    private UserRepository userRepository;

    private PostRepository postRepository;

    private PostService postService;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserPostsMapper userPostsMapper;

    @BeforeEach
    public void initComponents() {
        userRepository = mock(UserRepository.class);
        postRepository = mock(PostRepository.class);
        FileRepository fileRepository = mock(FileRepository.class);
        this.postService = new PostServiceImpl(postRepository, fileRepository, userRepository, postMapper, userPostsMapper);
    }

    @Test
    public void whenFindByUserIdsThenPostDtosByUser() {
        User user1 = new User(1L, "John", "pass", null);
        User user2 = new User(2L, "Kate", "pass", null);
        var users = List.of(user1, user2);
        when(userRepository.findAllById(anyList())).thenReturn(users);

        var posts = List.of(
                new Post(1L, "title1", null, null, true, user1, user2, null),
                new Post(2L, "title2", null, null, true, user1, user2, null),
                new Post(3L, "title3", null, null, true, user2, user1, null),
                new Post(4L, "title4", null, null, true, user2, user1, null));
        when(postRepository.findAllByFromUsers(users)).thenReturn(posts);

        UserPostsDto userPostsDto1 = new UserPostsDto(1L, "John", List.of(
                new PostDto(1L, "title1", null, null, true, "John", "Kate"),
                new PostDto(2L, "title2", null, null, true, "John", "Kate")));
        UserPostsDto userPostsDto2 = new UserPostsDto(2L, "Kate", List.of(
                new PostDto(3L, "title3", null, null, true, "Kate", "John"),
                new PostDto(4L, "title4", null, null, true, "Kate", "John")));

        var actual = postService.findAllByUsersId(List.of(1L, 2L));
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(userPostsDto1, userPostsDto2));
    }

}
