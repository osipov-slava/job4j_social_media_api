package ru.job4j.socialmediaapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.socialmediaapi.model.User;
import ru.job4j.socialmediaapi.service.UserService;

import java.util.List;

@Tag(name = "UsersController", description = "UsersController management APIs")
@AllArgsConstructor
@RestController
@RequestMapping("api/users")
public class UsersController {

    private final UserService userService;

    @Operation(
            summary = "Retrieve users",
            description = "Get all User objects. The response is the List of User",
            tags = {"Users", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(
                            array = @ArraySchema(schema = @Schema(implementation = User.class)),
                            mediaType = "application/json")})})

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAll() {
        return userService.findAll();
    }

}
