package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetUserDetailsByUsernameCommand;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetUserDetailsByUsernameCommandImpl implements GetUserDetailsByUsernameCommand {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<User> execute(String username) {
        return userRepository.findByUsername(username);
    }

}
