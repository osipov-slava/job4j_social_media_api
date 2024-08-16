package ru.job4j.socialmediaapi.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.job4j.socialmediaapi.model.*;

import java.time.LocalDateTime;
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

    @Autowired
    RelationshipRepository relationshipRepository;

    @AfterAll
    public void clearAllAfterAll() {
        fileRepository.deleteAll();
        relationshipRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Sql("/sql/insertUsersPostsScript.sql")
    public void whenRunSqlScriptThenGetPostsByUsers() {
        List<User> users = userRepository.findAll();
        users.remove(0);
        List<Post> posts = postRepository.findAllByFromUsers(users);

        assertThat(users.size()).isEqualTo(3);
        assertThat(posts.size()).isEqualTo(6);
    }

    @Nested
    public class NestedTestBlock {

        @BeforeEach
        public void clearAllBeforeEach() {
            fileRepository.deleteAll();
            relationshipRepository.deleteAll();
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

        private Post initFirstPost(User from, User to) {
            var post = new Post();
            post.setFromUser(from);
            post.setToUser(to);
            post.setTitle("Hi. Its my first message");
            post.setDescription("So. Would you like to go smwh?");
            post.setCreated(LocalDateTime.now()
                    .truncatedTo(ChronoUnit.SECONDS));
            post.setIsActive(true);
            return post;
        }

        private Post initSecondPost(User from, User to) {
            var post2 = new Post();
            post2.setFromUser(from);
            post2.setToUser(to);
            post2.setTitle("Hello");
            post2.setDescription("I'm busy now");
            post2.setCreated(LocalDateTime.now()
                    .truncatedTo(ChronoUnit.SECONDS));
            post2.setIsActive(true);
            return post2;
        }

        private List<Post> initPostsWithDifferentDates(User userFrom, User userTo) {
            List<Post> posts = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                var post = new Post();
                post.setFromUser(userFrom);
                post.setToUser(userTo);
                post.setTitle("title %d%n".formatted(i));
                post.setCreated(LocalDateTime.now()
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

            var post = initFirstPost(users.get(0), users.get(1));

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

            var post = initFirstPost(users.get(0), users.get(1));
            postRepository.save(post);

            var post2 = initSecondPost(users.get(1), users.get(0));
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

            var post = initFirstPost(users.get(0), users.get(1));
            postRepository.save(post);

            var post2 = initSecondPost(users.get(0), users.get(1));
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

            var after = LocalDateTime.now().minusDays(4);
            var before = LocalDateTime.now().minusHours(1);

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

            posts.sort(Comparator.comparing(Post::getCreated));
            var expected = posts.subList(3, 5);

            var actual = postRepository.getPostsByOrderByCreated(PageRequest.of(1, 3));
            assertThat(actual.getContent().size()).isEqualTo(2);
            assertThat(actual.getContent()).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        public void whenSavePostThenUpdatePostMessage() {
            var users = initUsers();

            var post = initFirstPost(users.get(0), users.get(1));
            postRepository.save(post);
            var result = postRepository.updatePostMessage("Title2", "Description2", post);
            assertThat(result).isEqualTo(1);

            var actual = postRepository.getPostById(post.getId());
            assertThat(actual.isPresent()).isTrue();
            assertThat(actual.get().getTitle()).isEqualTo("Title2");
            assertThat(actual.get().getDescription()).isEqualTo("Description2");
        }

        @Test
        public void whenSaveThenDelete() {
            var users = initUsers();

            var post = initFirstPost(users.get(0), users.get(1));
            postRepository.save(post);

            var actual = postRepository.findById(post.getId());
            assertThat(actual.isPresent()).isTrue();

            var result = postRepository.deletePostById(post.getId());
            assertThat(result).isEqualTo(1);
            actual = postRepository.findById(post.getId());
            assertThat(actual.isPresent()).isFalse();
        }

        @Test
        public void whenSavePostsThenFindAllSubscriberPosts() {
            List<User> users = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                users.add(new User(null, "email %d".formatted(i), "password %d".formatted(i), null));
            }
            userRepository.saveAll(users);

            var relationship1 = new Relationship(null, users.get(1), users.get(0), null);
            var relationship2 = new Relationship(null, users.get(2), users.get(0), Type.SUBSCRIBER);
            var relationship3 = new Relationship(null, users.get(3), users.get(0), Type.SUBSCRIBER);
            var relationships = List.of(relationship1, relationship2, relationship3);
            relationshipRepository.saveAll(relationships);
            System.out.println(relationships);

            var posts = initPostsWithDifferentDates(users.get(1), users.get(0));
            posts.get(1).setFromUser(users.get(2));
            posts.get(2).setFromUser(users.get(2));
            posts.get(3).setFromUser(users.get(3));
            posts.get(4).setFromUser(users.get(3));
            postRepository.saveAll(posts);

            List<Post> expected = new ArrayList<>();
            expected.add(posts.get(3));
            expected.add(posts.get(4));

            var actual = postRepository.getPostsAllSubscribers(PageRequest.of(1, 2), users.get(0));
            assertThat(actual.getContent().size()).isEqualTo(2);
            assertThat(actual.getContent()).usingRecursiveComparison().isEqualTo(expected);
        }

    }

}
