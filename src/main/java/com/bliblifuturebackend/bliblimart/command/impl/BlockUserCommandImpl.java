package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.BlockUserCommand;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.response.UserResponse;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BlockUserCommandImpl implements BlockUserCommand {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<UserResponse> execute(String id) {
        return userRepository.findById(id)
                .doOnSuccess(this::isNullOrAlreadyBlocked)
                .map(user -> {
                    user.setIsActive(false);
                    user.setIsBlocked(true);
                    return user;
                })
                .flatMap(user -> userRepository.save(user).map(User::createResponse));
    }

    private void isNullOrAlreadyBlocked(User user){
        if (user == null){
            throw new NullPointerException("User not found");
        }
        else if(user.getIsBlocked()){
            throw new IllegalArgumentException("User already blocked");
        }
    }

}
