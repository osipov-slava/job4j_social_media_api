package ru.job4j.socialmediaapi.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.socialmediaapi.dto.PostDto;
import ru.job4j.socialmediaapi.model.Post;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "post.fromUser.email", target = "fromUser")
    @Mapping(source = "post.toUser.email", target = "toUser")
    PostDto getModelFromEntity(Post post);

    @Mapping(source = "post.fromUser.email", target = "fromUser")
    @Mapping(source = "post.toUser.email", target = "toUser")
    List<PostDto> getModelsFromEntities(List<Post> post);

}
