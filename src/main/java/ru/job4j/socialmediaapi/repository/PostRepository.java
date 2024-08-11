package ru.job4j.socialmediaapi.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.job4j.socialmediaapi.model.Post;
import ru.job4j.socialmediaapi.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> getPostById(Long id);

    List<Post> getPostsByFromUser(User user);

    List<Post> getPostsByCreatedBetween(LocalDateTime from, LocalDateTime to);

    Page<Post> getPostsByOrderByCreated(Pageable pageable);

    @Query("""
                select p from Post as p
                join Relationship as r on p.fromUser = r.user
                where r.partner = :user and r.type = 'SUBSCRIBER'
                order by p.created desc
            """)
    Page<Post> getPostsAllSubscribers(Pageable pageable, @Param("user") User user);

    @Transactional
    @Modifying
    @Query("""
            update Post p
            set p.title = :#{#post.title},
            p.description = :#{#post.description},
            p.isActive = :#{#post.isActive}
            where p.id = :#{#post.id}
            """)
    int update(@Param("post") Post post);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("""
                update Post as p
                set p.title = :title, p.description = :description
                where p = :post
            """)
    int updatePostMessage(@Param("title") String title, @Param("description") String description, @Param("post") Post post);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("""
                delete from Post as p
                where p.id = ?1
            """)
    int deletePostById(Long id);

}
