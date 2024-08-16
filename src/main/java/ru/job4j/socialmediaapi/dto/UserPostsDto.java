package ru.job4j.socialmediaapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserPostsDto {

    private final Long id;

    private final String email;

    private final List<PostDto> postDtos;

}
