package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.LoginCommand;
import com.bliblifuturebackend.bliblimart.config.JwtTokenUtil;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.request.UserRequest;
import com.bliblifuturebackend.bliblimart.model.response.JwtResponse;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LoginCommandImpl implements LoginCommand {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public Mono<JwtResponse> execute(UserRequest request) {
        return Mono.fromCallable(request::getUsername)
                .flatMap(username -> userRepository.findByUsername(username)
                        .doOnSuccess(this::checkNullOrBlocked)
                        .map(user -> authenticateUser(user, request))
                );
    }

    private void checkNullOrBlocked(User user) {
        if (user == null){
            throw new SecurityException("Username or password is wrong!");
        }
        else if (user.getIsBlocked()){
            throw new SecurityException("User is blocked!");
        }
    }

    private JwtResponse authenticateUser(User user, UserRequest request) {
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())){
            user.setIsActive(true);
            userRepository.save(user).subscribe();
            String token = jwtTokenUtil.generateToken(user);
            return JwtResponse.builder().jwtToken(token).userResponse(user.createResponse()).build();
        }
        else{
            throw new SecurityException("Username or password is wrong!");
        }
    }
}
