package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.DeleteBlimartCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Blimart;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.repository.BlimartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteBlimartCommandImpl implements DeleteBlimartCommand {

    @Autowired
    private BlimartRepository blimartRepository;

    @Override
    public Mono<MessageResponse> execute(String id) {
        return blimartRepository.findById(id)
                .doOnSuccess(blimart -> {
                    checkNull(blimart);
                    blimartRepository.deleteById(blimart.getId()).subscribe();
                })
                .map(mark -> MessageResponse.SUCCESS);
    }

    private void checkNull(Blimart blimart) {
        if (blimart == null){
            throw new NullPointerException("Blimart not found!");
        }
    }
}
