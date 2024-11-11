package ru.job4j.socialmediaapi.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmediaapi.model.Relationship;
import ru.job4j.socialmediaapi.model.Type;
import ru.job4j.socialmediaapi.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RelationshipRepositoryTest {

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        relationshipRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterAll
    public void clearAll() {
        relationshipRepository.deleteAll();
        userRepository.deleteAll();
    }

    private List<User> initUsers() {
        var user1 = new User();
        user1.setUsername("John");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("password");
        userRepository.save(user1);

        var user2 = new User();
        user2.setUsername("Kate");
        user2.setEmail("kate.doe@example.com");
        user2.setPassword("password");
        userRepository.save(user2);
        return List.of(user1, user2);
    }

    @Test
    public void whenSaveRelationShipThenFindByUserAndPartner() {
        var users = initUsers();

        var relationship = new Relationship();
        relationship.setUser(users.get(0));
        relationship.setPartner(users.get(1));
        relationship.setType(Type.APPLICANT);
        relationshipRepository.save(relationship);

        var actual = relationshipRepository.findByUserAndPartner(users.get(1), users.get(0));
        assertThat(actual.isPresent()).isFalse();

        actual = relationshipRepository.findByUserAndPartner(users.get(0), users.get(1));
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(relationship);
    }

    @Test
    public void whenSaveRelationshipThenFindById() {
        var users = initUsers();

        var relationship = new Relationship();
        relationship.setUser(users.get(0));
        relationship.setPartner(users.get(1));
        relationship.setType(Type.APPLICANT);
        relationshipRepository.save(relationship);

        var actual = relationshipRepository.findById(relationship.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(relationship);
    }

    @Test
    public void whenUpdateRelationshipThenCheck() {
        var users = initUsers();

        var relationship = new Relationship();
        relationship.setUser(users.get(0));
        relationship.setPartner(users.get(1));
        relationship.setType(Type.APPLICANT);
        relationshipRepository.save(relationship);

        relationship.setType(Type.FRIEND);
        relationshipRepository.save(relationship);

        var actual = relationshipRepository.findById(relationship.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(relationship);
    }

    @Test
    public void whenFindAllThenReturnAllPosts() {
        var users = initUsers();

        var relationship = new Relationship();
        relationship.setUser(users.get(0));
        relationship.setPartner(users.get(1));
        relationship.setType(Type.APPLICANT);

        var relationship2 = new Relationship();
        relationship2.setUser(users.get(1));
        relationship2.setPartner(users.get(0));

        var expected = List.of(relationship, relationship2);
        relationshipRepository.saveAll(expected);

        var actual = relationshipRepository.findAll();
        assertThat(actual).hasSize(2);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void whenSaveRelationshipThenUpdateTypeByUsers() {
        var users = initUsers();

        var relationship = new Relationship();
        relationship.setUser(users.get(0));
        relationship.setPartner(users.get(1));
        relationship.setType(Type.APPLICANT);
        relationshipRepository.save(relationship);

        relationshipRepository.updateType(users.get(0), users.get(1), Type.FRIEND);
        relationship.setType(Type.FRIEND);

        var actual = relationshipRepository.findByUserAndPartner(users.get(0), users.get(1));
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(relationship);
    }

}
