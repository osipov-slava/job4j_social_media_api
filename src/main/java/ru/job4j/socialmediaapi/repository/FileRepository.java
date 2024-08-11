package ru.job4j.socialmediaapi.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.job4j.socialmediaapi.model.File;
import ru.job4j.socialmediaapi.model.Post;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("""
            delete from File f
            where f.id = :id
            """)
    int deleteFileById(@Param("id") Long id);

    List<File> findByPost(Post post);

}
