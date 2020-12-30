package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetProfileCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Profile;
import com.bliblifuturebackend.bliblimart.model.response.ProfileResponse;
import com.bliblifuturebackend.bliblimart.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetProfileCommandImpl implements GetProfileCommand {

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public Mono<ProfileResponse> execute(String id) {
        return Mono.fromCallable(() -> id)
                .flatMap(userId -> profileRepository.findByUserId(userId)
                        .doOnSuccess(this::checkNull))
                .map(Profile::createResponse);
    }

    private void checkNull(Profile profile) {
        if (profile == null){
            throw new NullPointerException("Profile not found");
        }
    }

}
