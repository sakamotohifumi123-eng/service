package com.example.blog.service;

import com.example.blog.dto.request.UpdateProfileRequest;
import com.example.blog.dto.response.ProfileResponse;

import com.example.blog.entity.Profile;

import com.example.blog.repository.ProfileRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(
            ProfileRepository profileRepository
    ) {
        this.profileRepository = profileRepository;
    }

    public ProfileResponse updateProfile(
            Integer userId,
            UpdateProfileRequest request
    ) {

        Profile profile =
                profileRepository.findById(userId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Profile not found"
                                )
                        );

        profile.setSkills(
                request.getSkills()
        );

        profile.setFree(
                request.getFree()
        );

        profile.setDesired(
                request.getDesired()
        );

        Profile savedProfile =
                profileRepository.save(profile);

        return new ProfileResponse(
                savedProfile.getName(),
                savedProfile.getMailAddress(),
                savedProfile.getSkills(),
                savedProfile.getFree(),
                savedProfile.getDesired(),

                savedProfile.getProject() != null
                        ? savedProfile.getProject().getProjectName()
                        : null,

                savedProfile.getRank() != null
                        ? savedProfile.getRank().getRankName()
                        : null
        );
    }
}
