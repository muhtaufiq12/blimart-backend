package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.UpdateMarkCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Mark;
import com.bliblifuturebackend.bliblimart.model.request.MarkRequest;
import com.bliblifuturebackend.bliblimart.model.response.MarkResponse;
import com.bliblifuturebackend.bliblimart.repository.MarkRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class UpdateMarkCommandImpl implements UpdateMarkCommand {

    @Autowired
    private MarkRepository markRepository;

    @Override
    public Mono<MarkResponse> execute(MarkRequest request) {
        return markRepository.findById(request.getId())
                .doOnSuccess(this::checkNull)
                .map(mark -> updateMark(request, mark))
                .flatMap(mark -> markRepository.save(mark))
                .map(Mark::createResponse);
    }

    private void checkNull(Mark mark) {
        if (mark == null){
            throw new NullPointerException("Mark not found!");
        }
    }

    private Mark updateMark(MarkRequest request, Mark mark) {
        BeanUtils.copyProperties(request, mark);

        String username = request.getRequester();

        Date date = new Date();
        mark.setUpdatedDate(date);
        mark.setUpdatedBy(username);

        return mark;
    }
}
