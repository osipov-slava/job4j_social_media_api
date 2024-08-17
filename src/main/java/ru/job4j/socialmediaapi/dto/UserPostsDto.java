package ru.job4j.socialmediaapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class UserPostsDto {

    private final Long id;

    @Schema(description = "Email by author messages", example = "fromuser@mail.com")
    private final String email;

    @Schema(description = "List all active posts by user")
    private final List<PostDto> postDtos;

}
