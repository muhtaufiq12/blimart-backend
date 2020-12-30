package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.CreateProfileCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Profile;
import com.bliblifuturebackend.bliblimart.model.request.ProfileRequest;
import com.bliblifuturebackend.bliblimart.model.response.ProfileResponse;
import com.bliblifuturebackend.bliblimart.repository.ProfileRepository;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class CreateProfileCommandImpl implements CreateProfileCommand {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<ProfileResponse> execute(ProfileRequest request) {
        return Mono.fromCallable(() -> createProfile(request))
                .flatMap(profile -> userRepository.findByUsername(request.getRequester())
                        .flatMap(user -> profileRepository.countByUserId(user.getId())
                                .map(countValue -> {
                                    isExists(countValue);
                                    profile.setUserId(user.getId());
                                    return profile;
                                }))
                )
                .flatMap(profile -> profileRepository.save(profile))
                .map(Profile::createResponse);
    }

    private void isExists(Long countValue) {
        if (countValue > 0){
            throw new IllegalArgumentException("Profile already exists!");
        }
    }

    private Profile createProfile(ProfileRequest request) throws ParseException {
        Profile profile = new Profile();
        BeanUtils.copyProperties(request, profile);
        profile.setId(UUID.randomUUID().toString());

        String username = request.getRequester();

        Date date = new Date();
        profile.setCreatedDate(date);
        profile.setCreatedBy(username);
        profile.setUpdatedDate(date);
        profile.setUpdatedBy(username);

        Date birthDate  = new SimpleDateFormat("yyyy-MM-dd").parse(request.getBirthDate());
        profile.setBirthDate(birthDate);
        return profile;
    }
}
