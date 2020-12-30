package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetMyProfileCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Profile;
import com.bliblifuturebackend.bliblimart.model.response.ProfileResponse;
import com.bliblifuturebackend.bliblimart.repository.ProfileRepository;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetMyProfileCommandImpl implements GetMyProfileCommand {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<ProfileResponse> execute(String username) {
        return userRepository.findByUsername(username)
                .flatMap(user -> profileRepository.findByUserId(user.getId())
                        .doOnSuccess(this::checkNull))
                .map(Profile::createResponse);
    }

    private void checkNull(Profile profile) {
        if (profile == null){
            throw new NullPointerException("Profile not found");
        }
    }

}
