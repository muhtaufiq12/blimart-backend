package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetAllBlimartsCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Blimart;
import com.bliblifuturebackend.bliblimart.model.response.BlimartResponse;
import com.bliblifuturebackend.bliblimart.repository.BlimartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GetAllBlimartsCommandImpl implements GetAllBlimartsCommand {

    @Autowired
    private BlimartRepository blimartRepository;

    @Override
    public Mono<List<BlimartResponse>> execute(String request) {
        return blimartRepository.findAll()
                .map(Blimart::createResponse)
                .collectList();
    }

}
