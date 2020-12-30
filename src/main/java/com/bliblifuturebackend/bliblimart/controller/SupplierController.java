package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.bliblifuturebackend.bliblimart.command.*;
import com.bliblifuturebackend.bliblimart.constant.ApiConstant;
import com.bliblifuturebackend.bliblimart.model.request.BaseRequest;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
import com.bliblifuturebackend.bliblimart.model.request.SupplierRequest;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.model.response.SupplierResponse;
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
@RequestMapping(ApiConstant.API_SUPPLIER)
@PreAuthorize("hasRole('ADMIN')")
public class SupplierController {

    @Autowired
    private CommandExecutor commandExecutor;

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<SupplierResponse>> getSupplierById(@PathVariable String id){
        return commandExecutor.execute(GetSupplierCommand.class, id)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<List<SupplierResponse>>> getAllSuppliers(@RequestParam("page") int page, @RequestParam("size") int size){
        PagingRequest request = PagingRequest.builder().page(page-1).size(size).build();
        return commandExecutor.execute(GetAllSuppliersCommand.class, request)
                .map(pagingResponse -> {
                    Response<List<SupplierResponse>> response = ResponseHelper.ok(pagingResponse.getData());
                    response.setPaging(pagingResponse.getPaging());
                    return response;
                })
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<SupplierResponse>> addSupplier(@RequestBody SupplierRequest request, Principal principal){
        request.setRequester(principal.getName());
        return commandExecutor.execute(CreateSupplierCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PutMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<SupplierResponse>> updateSupplier(@RequestBody SupplierRequest request, Principal principal){
        request.setRequester(principal.getName());
        return commandExecutor.execute(UpdateSupplierCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<MessageResponse>> deleteSupplier(@PathVariable("id") String id, Principal principal){
        BaseRequest request = new BaseRequest(id, principal.getName());
        return commandExecutor.execute(DeleteSupplierCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

}
