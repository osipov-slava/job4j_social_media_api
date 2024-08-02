package ru.job4j.socialmediaapi.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.socialmediaapi.model.Relationship;

public interface RelationshipRepository extends ListCrudRepository<Relationship, Long> {
}
