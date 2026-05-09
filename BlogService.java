package com.example.blog.service;

import com.example.blog.dto.request.CreateBlogRequest;
import com.example.blog.dto.response.BlogResponse;
import com.example.blog.dto.response.TagResponse;

import com.example.blog.entity.Blog;
import com.example.blog.entity.Tag;
import com.example.blog.entity.User;

import com.example.blog.repository.BlogRepository;
import com.example.blog.repository.TagRepository;
import com.example.blog.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BlogService {

    private final BlogRepository blogRepository;

    private final UserRepository userRepository;

    private final TagRepository tagRepository;

    public BlogService(
            BlogRepository blogRepository,
            UserRepository userRepository,
            TagRepository tagRepository
    ) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }

    public BlogResponse createBlog(
            Integer userId,
            CreateBlogRequest request
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );

        Blog blog = new Blog();

        blog.setBlogTitle(
                request.getBlogTitle()
        );

        blog.setBlogContents(
                request.getBlogContents()
        );

        blog.setCreateTime(
                LocalDateTime.now()
        );

        blog.setUpdateTime(
                LocalDateTime.now()
        );

        blog.setUser(user);

        List<Tag> tagList = new ArrayList<>();

        if (request.getTags() != null) {

            for (String tagName : request.getTags()) {

                Tag tag =
                        tagRepository.findByTagName(tagName)
                                .orElseGet(() -> {

                                    Tag newTag = new Tag();

                                    newTag.setTagName(tagName);

                                    return tagRepository.save(newTag);
                                });

                tagList.add(tag);
            }
        }

        blog.setTags(tagList);

        Blog savedBlog =
                blogRepository.save(blog);

        return convertToResponse(savedBlog);
    }

    public List<BlogResponse> getAllBlogs() {

        List<Blog> blogs =
                blogRepository
                        .findAllByOrderByCreateTimeDesc();

        return blogs.stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<BlogResponse> searchBlogs(
            String keyword
    ) {

        List<Blog> blogs =
                blogRepository
                        .findByBlogTitleContaining(keyword);

        return blogs.stream()
                .map(this::convertToResponse)
                .toList();
    }

    private BlogResponse convertToResponse(
            Blog blog
    ) {

        List<TagResponse> tags =
                blog.getTags()
                        .stream()
                        .map(tag ->
                                new TagResponse(
                                        tag.getTagId(),
                                        tag.getTagName()
                                )
                        )
                        .toList();

        return new BlogResponse(
                blog.getBlogId(),
                blog.getBlogTitle(),
                blog.getBlogContents(),
                blog.getCreateTime(),
                blog.getUpdateTime(),
                blog.getUser()
                        .getProfile()
                        .getName(),
                tags
        );
    }
}
