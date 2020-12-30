package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.bliblifuturebackend.bliblimart.command.CreateSupplyCommand;
import com.bliblifuturebackend.bliblimart.command.GetAllSuppliesCommand;
import com.bliblifuturebackend.bliblimart.constant.ApiConstant;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
import com.bliblifuturebackend.bliblimart.model.request.SupplyRequest;
import com.bliblifuturebackend.bliblimart.model.response.SupplyResponse;
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
@RequestMapping(ApiConstant.API_SUPPLY)
@PreAuthorize("hasRole('ADMIN')")
public class SupplyController {

    @Autowired
    private CommandExecutor commandExecutor;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<List<SupplyResponse>>> getAllSupplies(@RequestParam("page") int page, @RequestParam("size") int size){
        PagingRequest request = PagingRequest.builder().page(page-1).size(size).build();
        return commandExecutor.execute(GetAllSuppliesCommand.class, request)
                .map(pagingResponse -> {
                    Response<List<SupplyResponse>> response = ResponseHelper.ok(pagingResponse.getData());
                    response.setPaging(pagingResponse.getPaging());
                    return response;
                })
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<SupplyResponse>> addSupply(@RequestBody SupplyRequest request, Principal principal){
        request.setRequester(principal.getName());
        return commandExecutor.execute(CreateSupplyCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

}
