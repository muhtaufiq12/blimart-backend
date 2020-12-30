package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.UpdateBlimartCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Blimart;
import com.bliblifuturebackend.bliblimart.model.request.BlimartRequest;
import com.bliblifuturebackend.bliblimart.model.response.BlimartResponse;
import com.bliblifuturebackend.bliblimart.repository.BlimartRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class UpdateBlimartCommandImpl implements UpdateBlimartCommand {

    @Autowired
    private BlimartRepository blimartRepository;

    @Override
    public Mono<BlimartResponse> execute(BlimartRequest request) {
        return blimartRepository.findById(request.getId())
                .doOnSuccess(this::checkNull)
                .map(blimart -> updateBlimart(request, blimart))
                .flatMap(blimart -> blimartRepository.save(blimart))
                .map(Blimart::createResponse);
    }

    private void checkNull(Blimart blimart) {
        if (blimart == null){
            throw new NullPointerException("Blimart not found!");
        }
    }

    private Blimart updateBlimart(BlimartRequest request, Blimart blimart) {
        BeanUtils.copyProperties(request, blimart);

        String username = request.getRequester();

        Date date = new Date();
        blimart.setUpdatedDate(date);
        blimart.setUpdatedBy(username);

        return blimart;
    }
}
