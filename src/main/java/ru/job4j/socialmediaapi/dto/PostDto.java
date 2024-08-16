package ru.job4j.socialmediaapi.dto;

import lombok.Data;

@Data
public class PostDto {

    private final Long id;

    private final String title;

    private final String description;

    private final String created;

    private final Boolean isActive;

    private final String fromUser;

    private final String toUser;

}
