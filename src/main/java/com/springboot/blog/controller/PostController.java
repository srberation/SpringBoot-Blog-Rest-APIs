package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")

public class PostController
{
    private PostService postService;// here we're injecting interface not class, so we're making a loose coupling here
    // @Autowired omit this as it has only one constructor
    public PostController(PostService postService)
    {
        this.postService = postService;
    }

    //Create blog post RestApi
    @PostMapping
    public ResponseEntity<PostDto>createPost(@RequestBody PostDto postDto)
    {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }
    //Get all Post RestApi
    @GetMapping
    public PostResponse getAllPost(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value="pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    )
    {
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    //Get all post by id Restapi
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name ="id") long id)
    {
        return ResponseEntity.ok(postService.getPostByID(id));
    }

    //update post by id rest api
    @PutMapping("/{id}")

    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable(name="id") long id)
    {
        return  new ResponseEntity<>(postService.updatePost(postDto,id), HttpStatus.OK);
    }

    //delete post by id rest api

    @DeleteMapping("/{id}")

    public ResponseEntity<String> deletePost(@PathVariable(name="id") long id)
    {
        postService.deletePostById(id);
        return new ResponseEntity<>("Post entity deleted successfully", HttpStatus.OK);
    }
}
