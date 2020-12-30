package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.UnblockUserCommand;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.response.UserResponse;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UnblockUserCommandImpl implements UnblockUserCommand {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<UserResponse> execute(String id) {
        return userRepository.findById(id)
                .doOnSuccess(this::isNullOrNotBlocked)
                .map(user -> {
                    user.setIsActive(false);
                    user.setIsBlocked(false);
                    return user;
                })
                .flatMap(user -> userRepository.save(user).map(User::createResponse));
    }

    private void isNullOrNotBlocked(User user){
        if (user == null){
            throw new NullPointerException("User not found");
        }
        else if(!user.getIsBlocked()){
            throw new IllegalArgumentException("User isn't blocked");
        }
    }

}
