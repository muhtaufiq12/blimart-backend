package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.bliblifuturebackend.bliblimart.command.*;
import com.bliblifuturebackend.bliblimart.constant.ApiConstant;
import com.bliblifuturebackend.bliblimart.model.request.DirectionRequest;
import com.bliblifuturebackend.bliblimart.model.request.MarkRequest;
import com.bliblifuturebackend.bliblimart.model.request.RouteRequest;
import com.bliblifuturebackend.bliblimart.model.response.MarkResponse;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.model.response.RouteResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(ApiConstant.API_AR)
public class ARController {

    @Autowired
    private CommandExecutor commandExecutor;

    @GetMapping("/marks")
    public Mono<Response<List<MarkResponse>>> getAllMarks(){
        return commandExecutor.execute(GetAllMarksCommand.class, "")
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/route")
    public Mono<Response<RouteResponse>> getRoute(@RequestBody RouteRequest request){
        return commandExecutor.execute(GetRouteCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PutMapping("/{id}")
    public Mono<Response<RouteResponse>> getDirection(@PathVariable("id") String id, @RequestParam("mark") String mark){
        DirectionRequest request = DirectionRequest.builder().mark(mark).build();
        request.setId(id);
        return commandExecutor.execute(GetDirectionCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/marks")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<MarkResponse>> createNewMark(@RequestBody MarkRequest request, Principal principal){
        request.setRequester(principal.getName());
        return commandExecutor.execute(CreateNewMarkCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PutMapping("/marks")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<MarkResponse>> updateMark(@RequestBody MarkRequest request, Principal principal){
        request.setRequester(principal.getName());
        return commandExecutor.execute(UpdateMarkCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @DeleteMapping("/marks/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<MessageResponse>> deleteMark(@PathVariable String id){
        return commandExecutor.execute(DeleteMarkCommand.class, id)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

}
