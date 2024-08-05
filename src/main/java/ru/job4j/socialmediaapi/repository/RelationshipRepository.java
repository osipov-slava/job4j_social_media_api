package ru.job4j.socialmediaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.socialmediaapi.model.Relationship;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {

}
