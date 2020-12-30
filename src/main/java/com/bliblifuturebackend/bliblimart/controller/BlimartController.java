package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.bliblifuturebackend.bliblimart.command.*;
import com.bliblifuturebackend.bliblimart.constant.ApiConstant;
import com.bliblifuturebackend.bliblimart.model.request.BlimartRequest;
import com.bliblifuturebackend.bliblimart.model.response.BlimartResponse;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
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
@RequestMapping(ApiConstant.API_BLIMART)
public class BlimartController {

    @Autowired
    private CommandExecutor commandExecutor;

    @GetMapping()
    public Mono<Response<List<BlimartResponse>>> getAllBlimartss(){
        return commandExecutor.execute(GetAllBlimartsCommand.class, "")
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<BlimartResponse>> createNewBlimart(@RequestBody BlimartRequest request, Principal principal){
        request.setRequester(principal.getName());
        return commandExecutor.execute(CreateNewBlimartCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PutMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<BlimartResponse>> updateBlimart(@RequestBody BlimartRequest request, Principal principal){
        request.setRequester(principal.getName());
        return commandExecutor.execute(UpdateBlimartCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<MessageResponse>> deleteBlimart(@PathVariable String id){
        return commandExecutor.execute(DeleteBlimartCommand.class, id)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

}
