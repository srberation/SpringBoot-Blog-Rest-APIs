package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService
{
    private PostRepository postRepository;
    private ModelMapper mapper;

    //omit @Autowired annotation as it has only one constructor
    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper)
    {
        this.postRepository = postRepository;
        this.mapper=mapper;
    }

    @Override
    public PostDto createPost(PostDto postDto)
    {

        // convert DTO to entity
        Post Post = mapToEntity(postDto);
        com.springboot.blog.entity.Post newPost= postRepository.save(Post); // return entity which is saved in new variable type post

        //convert entity to DTO
        PostDto postResponse =mapToDto(newPost);

        // return postResponse now
        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir)
    {
        //Create Pageable instance
        Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNo,pageSize, sort);
        Page<Post>posts=postRepository.findAll(pageable);

        //get content for page object
        List<Post>listOfPosts=posts.getContent();

        //return listOfPosts.stream().map(Post -> mapToDto(Post)).collect(Collectors.toList());
        //get everything in a list of postdto class
        List<PostDto>content= listOfPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        //create instance of PostResponse dto
        PostResponse postResponse=new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostByID(long id)
    {
        Post post= postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id)
    {
        //get post by id from the database
        Post post= postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        com.springboot.blog.entity.Post updatedPost= postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePostById(long id)
    {
        //get post by id from the database
        Post post= postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        postRepository.delete(post);
    }



    //convert entity to dto
    private PostDto mapToDto(Post post)
    {
        PostDto postDto=mapper.map(post,PostDto.class);
        /*PostDto postDto= new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());*/
        return postDto;
    }

    //convert DTO to entity

    private Post mapToEntity(PostDto postDto)
    {
        Post post=mapper.map(postDto,Post.class);
       /* Post post=new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());*/
        return post;
    }
}
