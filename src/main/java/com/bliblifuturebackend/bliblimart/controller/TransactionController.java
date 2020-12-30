package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.bliblifuturebackend.bliblimart.command.*;
import com.bliblifuturebackend.bliblimart.constant.ApiConstant;
import com.bliblifuturebackend.bliblimart.model.request.BaseRequest;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
import com.bliblifuturebackend.bliblimart.model.request.PaymentRequest;
import com.bliblifuturebackend.bliblimart.model.request.TransactionRequest;
import com.bliblifuturebackend.bliblimart.model.response.TransactionDetailResponse;
import com.bliblifuturebackend.bliblimart.model.response.TransactionResponse;
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
@RequestMapping()
public class TransactionController {

    @Autowired
    private CommandExecutor commandExecutor;

    @GetMapping(value = ApiConstant.API_TRANSACTION + "/{id}")
    @PreAuthorize("hasRole('USER')")
    public Mono<Response<TransactionResponse>> getTransactionById(@PathVariable String id, Principal principal){
        BaseRequest request = new BaseRequest(id, principal.getName());
        return commandExecutor.execute(GetTransactionCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping(value = ApiConstant.API_TRANSACTION + "/me")
    @PreAuthorize("hasRole('USER')")
    public Mono<Response<List<TransactionResponse>>> getActiveTransaction(Principal principal){
        return commandExecutor.execute(GetUserActiveTransactionCommand.class, principal.getName())
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping(value = ApiConstant.API_TRANSACTION + "/history")
    @PreAuthorize("hasRole('USER')")
    public Mono<Response<List<TransactionDetailResponse>>> getHistoryTransaction(@RequestParam("page") int page, @RequestParam("size") int size, Principal principal){
        PagingRequest request = PagingRequest.builder().page(page-1).size(size).build();
        request.setRequester(principal.getName());
        return commandExecutor.execute(GetUserHistoryTransactionCommand.class, request)
                .map(pagingResponse -> {
                    Response<List<TransactionDetailResponse>> response = ResponseHelper.ok(pagingResponse.getData());
                    response.setPaging(pagingResponse.getPaging());
                    return response;
                })
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping(value = ApiConstant.BASE_API + "/checkout")
    @PreAuthorize("hasRole('USER')")
    public Mono<Response<TransactionResponse>> addTransaction(@RequestBody TransactionRequest request, Principal principal){
        request.setRequester(principal.getName());
        return commandExecutor.execute(CreateTransactionCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping(value = ApiConstant.API_TRANSACTION + "/payment/{id}")
    @PreAuthorize("hasRole('USER')")
    public Mono<Response<TransactionResponse>> payment(@PathVariable String id, Principal principal){
        PaymentRequest request = PaymentRequest.builder().username(principal.getName()).transactionId(id).build();
        return commandExecutor.execute(PaymentCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @DeleteMapping(value = ApiConstant.API_TRANSACTION + "/{id}")
    @PreAuthorize("hasRole('USER')")
    public Mono<Response<TransactionResponse>> cancelTransaction(@PathVariable String id, Principal principal){
        BaseRequest request = new BaseRequest(id, principal.getName());
        return commandExecutor.execute(CancelTransactionCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

}
