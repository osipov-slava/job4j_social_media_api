package ru.job4j.socialmediaapi.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.job4j.socialmediaapi.model.Relationship;
import ru.job4j.socialmediaapi.model.Type;
import ru.job4j.socialmediaapi.model.User;

import java.util.Optional;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {

    Optional<Relationship> findByUserAndPartner(User user, User partner);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("""
                update Relationship as r
                set r.type = :type
                where r.user = :user and r.partner = :partner
            """)
    void updateType(User user, User partner, Type type);

}
