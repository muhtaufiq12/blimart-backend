package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetAllMarksCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Mark;
import com.bliblifuturebackend.bliblimart.model.response.MarkResponse;
import com.bliblifuturebackend.bliblimart.repository.MarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GetAllMarksCommandImpl implements GetAllMarksCommand {

    @Autowired
    private MarkRepository markRepository;

    @Override
    public Mono<List<MarkResponse>> execute(String request) {
        return markRepository.findAll()
                .map(Mark::createResponse)
                .collectList();
    }

}
