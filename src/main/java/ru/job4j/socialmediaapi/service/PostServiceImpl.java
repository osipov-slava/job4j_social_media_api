package ru.job4j.socialmediaapi.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.socialmediaapi.dto.UserPostsDto;
import ru.job4j.socialmediaapi.mapstruct.PostMapper;
import ru.job4j.socialmediaapi.mapstruct.UserPostsMapper;
import ru.job4j.socialmediaapi.model.File;
import ru.job4j.socialmediaapi.model.Post;
import ru.job4j.socialmediaapi.model.User;
import ru.job4j.socialmediaapi.repository.FileRepository;
import ru.job4j.socialmediaapi.repository.PostRepository;
import ru.job4j.socialmediaapi.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final FileRepository fileRepository;

    private final UserRepository userRepository;

    private final PostMapper postMapper;

    private final UserPostsMapper userPostsMapper;

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

    @Override
    public List<UserPostsDto> findAllByUsersId(List<Long> userIds) {
        List<User> users = userRepository.findAllById(userIds);
        List<Post> posts = postRepository.findAllByFromUsers(users);

        Map<User, List<Post>> postsByUserMap = new HashMap<>();
        for (Post post : posts) {
            if (!postsByUserMap.containsKey(post.getFromUser())) {
                postsByUserMap.put(post.getFromUser(), new ArrayList<>());
            }
            postsByUserMap.get(post.getFromUser()).add(post);
        }

        List<UserPostsDto> userPostsDtos = new ArrayList<>();
        for (Map.Entry<User, List<Post>> postByUsers : postsByUserMap.entrySet()) {
            userPostsDtos.add(userPostsMapper.getModelFromEntity(postByUsers.getKey(),
                    postMapper.getModelsFromEntities(postByUsers.getValue())));
        }

        return userPostsDtos;
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
