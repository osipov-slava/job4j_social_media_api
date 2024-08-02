package ru.job4j.socialmediaapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmediaapi.model.File;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    @BeforeEach
    public void setUp() {
        fileRepository.deleteAll();
    }

    @Test
    public void whenSaveFileThenFindById() {
        var file = new File();
        file.setPath("folder/");
        file.setName("file1.txt");
        fileRepository.save(file);
        var foundFile = fileRepository.findById(file.getId());
        assertThat(foundFile).isPresent();
        assertThat(foundFile.get()).usingRecursiveComparison().isEqualTo(file);
    }

    @Test
    public void whenFindAllThenReturnAllFiles() {
        var file1 = new File();
        file1.setPath("folder/");
        file1.setName("file1.txt");
        var file2 = new File();
        file2.setPath("folder/");
        file2.setName("file2.txt");
        fileRepository.save(file1);
        fileRepository.save(file2);
        var expected = List.of(file1, file2);
        var actual = fileRepository.findAll();
        assertThat(actual).hasSize(2);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

}