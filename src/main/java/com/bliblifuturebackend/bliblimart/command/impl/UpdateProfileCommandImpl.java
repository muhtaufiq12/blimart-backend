package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.UpdateProfileCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Profile;
import com.bliblifuturebackend.bliblimart.model.request.ProfileRequest;
import com.bliblifuturebackend.bliblimart.model.response.ProfileResponse;
import com.bliblifuturebackend.bliblimart.repository.ProfileRepository;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class UpdateProfileCommandImpl implements UpdateProfileCommand {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<ProfileResponse> execute(ProfileRequest request) {
        return userRepository.findByUsername(request.getRequester())
                .flatMap(user -> profileRepository.findByUserId(user.getId())
                        .doOnSuccess(this::checkNull)
                )
                .map(profile -> updateProfile(request, profile))
                .flatMap(profile -> profileRepository.save(profile))
                .map(Profile::createResponse);
    }

    private void checkNull(Profile profile) {
        if (profile == null){
            throw new NullPointerException("Profile not found");
        }
    }

    private Profile updateProfile(ProfileRequest request, Profile profile) {
        BeanUtils.copyProperties(request, profile);
        try {
            Date date  = new SimpleDateFormat("yyyy-MM-dd").parse(request.getBirthDate());
            profile.setBirthDate(date);
        }
        catch (Exception e){
            log.error(e.getMessage(), e);
        }

        String username = request.getRequester();
        Date date = new Date();
        profile.setUpdatedDate(date);
        profile.setUpdatedBy(username);

        return profile;
    }
}
