package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetDirectionCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Route;
import com.bliblifuturebackend.bliblimart.model.request.DirectionRequest;
import com.bliblifuturebackend.bliblimart.model.response.RouteResponse;
import com.bliblifuturebackend.bliblimart.model.response.responseUtil.RouteResponseUtil;
import com.bliblifuturebackend.bliblimart.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetDirectionCommandImpl implements GetDirectionCommand {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private RouteResponseUtil routeResponseUtil;

    @Override
    public Mono<RouteResponse> execute(DirectionRequest request) {
        return routeRepository.findById(request.getId())
                .doOnSuccess(this::checkNull)
                .map(route -> routeResponseUtil.getDirection(route, request.getMark()));
    }

    private void checkNull(Route route) {
        if (route == null){
            throw new NullPointerException("Route not exists!");
        }
    }

}
