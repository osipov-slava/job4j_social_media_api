package ru.job4j.socialmediaapi.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.socialmediaapi.model.File;
import ru.job4j.socialmediaapi.model.Post;
import ru.job4j.socialmediaapi.repository.FileRepository;
import ru.job4j.socialmediaapi.repository.PostRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final FileRepository fileRepository;

    @Override
    public Post save(Post post, List<File> files) {
        post.setCreated(LocalDateTime.now()
                .truncatedTo(ChronoUnit.SECONDS));
        post.setFiles(files);
        return postRepository.save(post);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Transactional
    @Override
    public boolean update(Post post, List<File> files) {
        var oldFiles = fileRepository.findByPost(post);
        var setNewFilesId = files.stream()
                .map(File::getId)
                .collect(Collectors.toSet());

        List<File> deleteList = new ArrayList<>();
        oldFiles.forEach(oldFile -> {
            if (!setNewFilesId.contains(oldFile.getId())) {
                deleteList.add(oldFile);
            }
        });
        fileRepository.deleteAll(deleteList);

        files.forEach(file -> file.setPost(post));
        fileRepository.saveAllAndFlush(files);
        return postRepository.update(post) > 0;
    }

    public void updateCascade(Post post, List<File> files) {
        post.setFiles(files);
        postRepository.saveAndFlush(post);
    }

    @Override
    public void delete(Post post) {
        postRepository.delete(post);
    }

    @Override
    public boolean deleteById(Long id) {
        return postRepository.deletePostById(id) > 0;
    }

}
