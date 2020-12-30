package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.LogoutCommand;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.response.JwtResponse;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Service
public class LogoutCommandImpl implements LogoutCommand {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<JwtResponse> execute(Principal principal) {
        return Mono.fromCallable(principal::getName)
                .flatMap(username -> userRepository.findByUsername(username))
                .doOnSuccess(this::checkNull)
                .flatMap(user -> {
                    user.setIsActive(false);
                    return userRepository.save(user);
                })
                .map(user -> JwtResponse.builder().build());
    }

    private void checkNull(User user) {
        if (user == null){
            throw new NullPointerException("User not found!");
        }
    }

}
