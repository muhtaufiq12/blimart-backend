package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetAllUsersCommand;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
import com.bliblifuturebackend.bliblimart.model.response.PagingResponse;
import com.bliblifuturebackend.bliblimart.model.response.UserResponse;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetAllUsersCommandImpl implements GetAllUsersCommand {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<PagingResponse<UserResponse>> execute(PagingRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        PagingResponse<UserResponse> response = new PagingResponse<>();
        return userRepository.findAll(pageable)
                .map(User::createResponse)
                .collectList()
                .flatMap(userResponses -> {
                    response.setData(userResponses);
                    return userRepository.count();
                })
                .map(total -> response.getPagingResponse(request, Math.toIntExact(total)));
    }

}
