package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.bliblifuturebackend.bliblimart.command.CreateCategoryCommand;
import com.bliblifuturebackend.bliblimart.command.GetAllCategoriesCommand;
import com.bliblifuturebackend.bliblimart.command.UpdateCategoryCommand;
import com.bliblifuturebackend.bliblimart.constant.ApiConstant;
import com.bliblifuturebackend.bliblimart.model.request.CategoryRequest;
import com.bliblifuturebackend.bliblimart.model.response.CategoryResponse;
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
@RequestMapping(ApiConstant.API_CATEGORY)
public class CategoryController {

    @Autowired
    private CommandExecutor commandExecutor;

    @GetMapping()
    public Mono<Response<List<CategoryResponse>>> getAllCategory(){
        return commandExecutor.execute(GetAllCategoriesCommand.class, "")
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<CategoryResponse>> addCategory(@RequestBody CategoryRequest request, Principal principal){
        request.setRequester(principal.getName());
        return commandExecutor.execute(CreateCategoryCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PutMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<CategoryResponse>> updateCategory(@RequestBody CategoryRequest request, Principal principal){
        request.setRequester(principal.getName());
        return commandExecutor.execute(UpdateCategoryCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

}
