package com.example.blog.service;

import com.example.blog.dto.response.TagResponse;

import com.example.blog.entity.Tag;

import com.example.blog.repository.TagRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(
            TagRepository tagRepository
    ) {
        this.tagRepository = tagRepository;
    }

    public List<TagResponse> getAllTags() {

        List<Tag> tags =
                tagRepository.findAll();

        return tags.stream()
                .map(tag ->
                        new TagResponse(
                                tag.getTagId(),
                                tag.getTagName()
                        )
                )
                .toList();
    }
}
