package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.RegisterCommand;
import com.bliblifuturebackend.bliblimart.constant.UserConstant;
import com.bliblifuturebackend.bliblimart.model.entity.Profile;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.request.UserRequest;
import com.bliblifuturebackend.bliblimart.model.response.UserResponse;
import com.bliblifuturebackend.bliblimart.repository.ProfileRepository;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class RegisterCommandImpl implements RegisterCommand {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Mono<UserResponse> execute(UserRequest request) {
        return Mono.fromCallable(() -> createUser(request))
                .flatMap(user -> userRepository.findByUsername(request.getUsername())
                        .doOnSuccess(this::isExists)
                        .switchIfEmpty(Mono.just(createUser(request))))
                .flatMap(user -> userRepository.save(user))
                .doOnSuccess(user -> {
                    try {
                        profileRepository.save(createProfile(user)).subscribe();
                    } catch (ParseException e) {
                        log.error(e.getMessage(), e);
                    }
                })
                .map(User::createResponse);
    }

    private void isExists(User user) {
        if (user != null){
            throw new IllegalArgumentException("Username already Exists");
        }
    }

    private Profile createProfile(User user) throws ParseException {
        Profile profile = new Profile();
        profile.setUserId(user.getId());
        profile.setId(UUID.randomUUID().toString());

        String username = user.getUsername();

        Date date = new Date();
        profile.setCreatedDate(date);
        profile.setCreatedBy(username);
        profile.setUpdatedDate(date);
        profile.setUpdatedBy(username);

        String defaultDate = "2000-01-01";

        Date birthDate  = new SimpleDateFormat("yyyy-MM-dd").parse(defaultDate);
        profile.setBirthDate(birthDate);
        return profile;
    }

    private User createUser(UserRequest request){
        User user = User.builder()
                .isActive(false)
                .isAdmin(request.isAdmin())
                .isBlocked(false)
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .build();

        String id = UUID.randomUUID().toString();
        user.setId(id);
        user.setPhotoUrl(UserConstant.PHOTO_URL + id);

        Date date = new Date();
        user.setCreatedDate(date);
        user.setCreatedBy(request.getUsername());
        user.setUpdatedDate(date);
        user.setUpdatedBy(request.getUsername());

        return user;
    }
}
