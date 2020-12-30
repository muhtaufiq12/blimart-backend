package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.CreateNewBlimartCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Blimart;
import com.bliblifuturebackend.bliblimart.model.request.BlimartRequest;
import com.bliblifuturebackend.bliblimart.model.response.BlimartResponse;
import com.bliblifuturebackend.bliblimart.repository.BlimartRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

@Service
public class CreateNewBlimartCommandImpl implements CreateNewBlimartCommand {

    @Autowired
    private BlimartRepository blimartRepository;

    @Override
    public Mono<BlimartResponse> execute(BlimartRequest request) {
        return blimartRepository.findByName(request.getName())
                .doOnSuccess(this::checkIfExists)
                .switchIfEmpty(Mono.just(createBlimart(request)))
                .flatMap(blimart -> blimartRepository.save(blimart))
                .map(Blimart::createResponse);
    }

    private void checkIfExists(Blimart blimart) {
        if (blimart != null){
            throw new IllegalArgumentException("Blimart with the same name already exists!");
        }
    }

    private Blimart createBlimart(BlimartRequest request) {
        Blimart blimart = new Blimart();

        BeanUtils.copyProperties(request, blimart);
        blimart.setId(UUID.randomUUID().toString());

        String username = request.getRequester();

        Date date = new Date();
        blimart.setCreatedDate(date);
        blimart.setCreatedBy(username);
        blimart.setUpdatedDate(date);
        blimart.setUpdatedBy(username);

        return blimart;
    }
}
