package ru.job4j.socialmediaapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PostDto {

    private final Long id;

    @Schema(description = "Short description", example = "New topic")
    private final String title;

    @Schema(description = "Long description", example = "Let's tall about it")
    private final String description;

    private final String created;

    private final Boolean isActive;

    @Schema(description = "Email by user who send a message", example = "fromuser@mail.com")
    private final String fromUser;

    @Schema(description = "Email by user who receive a message", example = "touser@mail.com")
    private final String toUser;

}
