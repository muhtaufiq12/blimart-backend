package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.CreateNewMarkCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Mark;
import com.bliblifuturebackend.bliblimart.model.request.MarkRequest;
import com.bliblifuturebackend.bliblimart.model.response.MarkResponse;
import com.bliblifuturebackend.bliblimart.repository.MarkRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

@Service
public class CreateNewMarkCommandImpl implements CreateNewMarkCommand {

    @Autowired
    private MarkRepository markRepository;

    @Override
    public Mono<MarkResponse> execute(MarkRequest request) {
        return markRepository.findByName(request.getName())
                .doOnSuccess(this::checkIfExists)
                .switchIfEmpty(Mono.just(createMark(request)))
                .flatMap(mark -> markRepository.save(mark))
                .map(Mark::createResponse);
    }

    private void checkIfExists(Mark mark) {
        if (mark != null){
            throw new IllegalArgumentException("Mark with the same name already exists!");
        }
    }

    private Mark createMark(MarkRequest request) {
        Mark mark = new Mark();

        BeanUtils.copyProperties(request, mark);
        mark.setId(UUID.randomUUID().toString());

        String username = request.getRequester();

        Date date = new Date();
        mark.setCreatedDate(date);
        mark.setCreatedBy(username);
        mark.setUpdatedDate(date);
        mark.setUpdatedBy(username);

        return mark;
    }
}
