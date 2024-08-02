package ru.job4j.socialmediaapi.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmediaapi.model.File;
import ru.job4j.socialmediaapi.model.Post;
import ru.job4j.socialmediaapi.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    @BeforeEach
    public void setUp() {
        fileRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterAll
    public void clearAll() {
        fileRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    private List<User> initUsers() {
        var user1 = new User();
        user1.setEmail("john.doe@example.com");
        user1.setPassword("password");
        userRepository.save(user1);

        var user2 = new User();
        user2.setEmail("kate.doe@example.com");
        user2.setPassword("password");
        userRepository.save(user2);
        return List.of(user1, user2);
    }

    @Test
    public void whenSavePostThenFindById() {
        var users = initUsers();

        var post = new Post();
        post.setFromUser(users.get(0));
        post.setToUser(users.get(1));
        post.setTitle("Hi. Its my first message");
        post.setDescription("So. Would you like to go smwh?");
        post.setCreated(LocalDateTime.now(ZoneId.of("UTC"))
                .truncatedTo(ChronoUnit.SECONDS));
        post.setIsActive(true);

        var file1 = new File();
        file1.setPath("folder/");
        file1.setName("file1.txt");
        file1.setPost(post);
        var file2 = new File();
        file2.setPath("folder/");
        file2.setName("file2.txt");
        file2.setPost(post);
        var files = List.of(file1, file2);

        post.setFiles(files);
        postRepository.save(post);

        var actual = postRepository.findById(post.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(post);
    }

    @Test
    public void whenFindAllThenReturnAllPosts() {
        var users = initUsers();

        var post = new Post();
        post.setFromUser(users.get(0));
        post.setToUser(users.get(1));
        post.setTitle("Hi. Its my first message");
        post.setDescription("So. Would you like to go smwh?");
        post.setCreated(LocalDateTime.now(ZoneId.of("UTC"))
                .truncatedTo(ChronoUnit.SECONDS));
        post.setIsActive(true);
        postRepository.save(post);

        var post2 = new Post();
        post2.setFromUser(users.get(1));
        post2.setToUser(users.get(0));
        post2.setTitle("Hello");
        post2.setDescription("I'm busy now");
        post2.setCreated(LocalDateTime.now(ZoneId.of("UTC"))
                .truncatedTo(ChronoUnit.SECONDS));
        post2.setIsActive(true);
        postRepository.save(post2);
        var expected = List.of(post, post2);

        var posts = postRepository.findAll();
        assertThat(posts).hasSize(2);
        assertThat(posts).usingRecursiveComparison().isEqualTo(expected);
    }

}
