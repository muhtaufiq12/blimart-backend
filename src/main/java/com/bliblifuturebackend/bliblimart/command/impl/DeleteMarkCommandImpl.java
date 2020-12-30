package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.DeleteMarkCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Mark;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.repository.MarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteMarkCommandImpl implements DeleteMarkCommand {

    @Autowired
    private MarkRepository markRepository;

    @Override
    public Mono<MessageResponse> execute(String id) {
        return markRepository.findById(id)
                .doOnSuccess(mark -> {
                    checkNull(mark);
                    markRepository.deleteById(mark.getId()).subscribe();
                })
                .map(mark -> MessageResponse.SUCCESS);
    }

    private void checkNull(Mark mark) {
        if (mark == null){
            throw new NullPointerException("Mark not found!");
        }
    }
}
