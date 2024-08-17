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
import org.springframework.web.bind.annotation.*;
import ru.job4j.socialmediaapi.dto.UserPostsDto;
import ru.job4j.socialmediaapi.model.Post;
import ru.job4j.socialmediaapi.service.PostService;

import java.util.List;

@Tag(name = "PostsController", description = "PostsController management APIs")
@RestController
@AllArgsConstructor
@RequestMapping("api/posts")
public class PostsController {

    private final PostService postService;

    @Operation(
            summary = "Retrieve posts",
            description = "Get all Post objects. The response is the List of Post",
            tags = {"Posts", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(
                            array = @ArraySchema(schema = @Schema(implementation = Post.class)),
                            mediaType = "application/json")})})

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<Post> findAll() {
        return postService.findAll();
    }

    @Operation(
            summary = "Retrieve active posts by list of users Id (authors)",
            description = "Get PostDto objects by specifying its users Id. The response is the List of UserPostDto",
            tags = {"Posts", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(
                            array = @ArraySchema(schema = @Schema(implementation = UserPostsDto.class)),
                            mediaType = "application/json")})})

    @PostMapping("/byUsers")
    @ResponseStatus(HttpStatus.OK)
    List<UserPostsDto> findAllByUsersId(@RequestBody List<Long> userIds) {
        return postService.findAllByUsersId(userIds);
    }

}

