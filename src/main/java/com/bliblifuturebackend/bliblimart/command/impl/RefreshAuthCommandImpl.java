package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.RefreshAuthCommand;
import com.bliblifuturebackend.bliblimart.config.JwtTokenUtil;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.response.JwtResponse;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Service
public class RefreshAuthCommandImpl implements RefreshAuthCommand {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public Mono<JwtResponse> execute(Principal principal) {
        return Mono.fromCallable(principal::getName)
                .flatMap(username -> userRepository.findByUsername(username))
                .doOnSuccess(this::checkNullOrBlocked)
                .map(this::getToken);
    }

    private void checkNullOrBlocked(User user) {
        if (user == null){
            throw new SecurityException("Unauthorized");
        }
        else if (user.getIsBlocked()){
            user.setIsActive(false);
            userRepository.save(user).subscribe();
            throw new SecurityException("User is blocked!");
        }
    }

    private JwtResponse getToken(User user) {
        String token = jwtTokenUtil.generateToken(user);
        return JwtResponse.builder().jwtToken(token).userResponse(user.createResponse()).build();
    }
}
