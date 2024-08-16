package ru.job4j.socialmediaapi.mapstruct;

import org.mapstruct.Mapper;
import ru.job4j.socialmediaapi.dto.PostDto;
import ru.job4j.socialmediaapi.dto.UserPostsDto;
import ru.job4j.socialmediaapi.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserPostsMapper {

    UserPostsDto getModelFromEntity(User user, List<PostDto> postDtos);

}
