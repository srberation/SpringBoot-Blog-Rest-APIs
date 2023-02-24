package com.springboot.blog.payload;

import lombok.Data;

import java.util.Set;

@Data
public class PostDto
{
    private Long id;
    // title should not be null  or empty
    // title should have at least 2 characters
    //Santitycheck -testing
    //1- notnull, notempty, size
    //2- No. of comments( integer) cannot be null, and in range
    //
    private String title;
    private String description;
    private String Content;
    private Set<CommentDto> comments;
}
