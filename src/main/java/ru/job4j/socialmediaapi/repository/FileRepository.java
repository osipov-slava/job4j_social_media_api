package ru.job4j.socialmediaapi.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.socialmediaapi.model.File;

public interface FileRepository extends ListCrudRepository<File, Long> {
}
