package com.example.blog.service;

import com.example.blog.dto.request.CreateUserRequest;
import com.example.blog.dto.response.ProfileResponse;
import com.example.blog.dto.response.UserResponse;
import com.example.blog.entity.Profile;
import com.example.blog.entity.User;
import com.example.blog.repository.ProfileRepository;
import com.example.blog.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;

    public UserService(
            UserRepository userRepository,
            ProfileRepository profileRepository
    ) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    public UserResponse createUser(
            CreateUserRequest request
    ) {

        User user = new User();

        user.setIsManager(false);
        user.setPassword(request.getPassword());

        User savedUser = userRepository.save(user);

        Profile profile = new Profile();

        profile.setUser(savedUser);

        profile.setName(request.getName());

        profile.setMailAddress(
                request.getMailAddress()
        );

        profileRepository.save(profile);

        ProfileResponse profileResponse =
                new ProfileResponse(
                        profile.getName(),
                        profile.getMailAddress(),
                        profile.getSkills(),
                        profile.getFree(),
                        profile.getDesired(),
                        null,
                        null
                );

        return new UserResponse(
                savedUser.getUserId(),
                savedUser.getIsManager(),
                profileResponse
        );
    }

    public UserResponse getUser(
            Integer userId
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );

        Profile profile = user.getProfile();

        ProfileResponse profileResponse =
                new ProfileResponse(
                        profile.getName(),
                        profile.getMailAddress(),
                        profile.getSkills(),
                        profile.getFree(),
                        profile.getDesired(),

                        profile.getProject() != null
                                ? profile.getProject().getProjectName()
                                : null,

                        profile.getRank() != null
                                ? profile.getRank().getRankName()
                                : null
                );

        return new UserResponse(
                user.getUserId(),
                user.getIsManager(),
                profileResponse
        );
    }
}
