package ru.job4j.socialmediaapi.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import ru.job4j.socialmediaapi.model.File;
import ru.job4j.socialmediaapi.model.Post;
import ru.job4j.socialmediaapi.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
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

    private List<Post> initPostsWithDifferentDates(User userFrom, User userTo) {
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            var post = new Post();
            post.setFromUser(userFrom);
            post.setToUser(userTo);
            post.setTitle("title %d%n".formatted(i));
            post.setCreated(LocalDateTime.now(ZoneId.of("UTC"))
                    .truncatedTo(ChronoUnit.SECONDS)
                    .minusDays(i));
            post.setIsActive(true);
            posts.add(post);
        }
        return posts;
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

    @Test
    public void whenSavePostsThenFindByAuthor() {
        var users = initUsers();
        var userFrom = users.get(0);
        var userTo = users.get(1);

        var post = new Post();
        post.setFromUser(userFrom);
        post.setToUser(userTo);
        post.setTitle("Hi. Its my first message");
        post.setDescription("So. Would you like to go smwh?");
        post.setCreated(LocalDateTime.now(ZoneId.of("UTC"))
                .truncatedTo(ChronoUnit.SECONDS));
        post.setIsActive(true);
        postRepository.save(post);

        var post2 = new Post();
        post2.setFromUser(userFrom);
        post2.setToUser(userTo);
        post2.setTitle("Where are you?");
        post2.setCreated(LocalDateTime.now(ZoneId.of("UTC"))
                .truncatedTo(ChronoUnit.SECONDS));
        post2.setIsActive(true);
        postRepository.save(post2);

        var expected = List.of(post, post2);
        var posts = postRepository.getPostsByFromUser(userFrom);
        assertThat(posts).hasSize(2);
        assertThat(posts).usingRecursiveComparison().isEqualTo(expected);

        posts = postRepository.getPostsByFromUser(userTo);
        assertThat(posts).hasSize(0);
    }

    @Test
    public void whenSavePostsThenFindBetweenDates() {
        var users = initUsers();
        List<Post> posts = initPostsWithDifferentDates(users.get(0), users.get(1));
        postRepository.saveAll(posts);

        var after = LocalDateTime.now(ZoneId.of("UTC"))
                .minusDays(4);
        var before = LocalDateTime.now(ZoneId.of("UTC"))
                .minusHours(1);

        var expected = posts.subList(1, 4);
        var actual = postRepository.getPostsByCreatedBetween(after, before);
        assertThat(actual).hasSize(3);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void whenSavePostsThenFindPageOrderByDate() {
        var users = initUsers();
        List<Post> posts = initPostsWithDifferentDates(users.get(0), users.get(1));
        postRepository.saveAll(posts);

        posts.sort(new Comparator<Post>() {
            public int compare(Post o1, Post o2) {
                return o1.getCreated().compareTo(o2.getCreated());
            }
        });
        var expected = posts.subList(3, 5);

        var actual = postRepository.getPostsByOrderByCreated(PageRequest.of(1, 3));
        assertThat(actual.getContent().size()).isEqualTo(2);
        assertThat(actual.getContent()).usingRecursiveComparison().isEqualTo(expected);
    }

}
