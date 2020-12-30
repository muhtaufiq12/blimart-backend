package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.bliblifuturebackend.bliblimart.command.CreateCartCommand;
import com.bliblifuturebackend.bliblimart.command.DeleteCartsCommand;
import com.bliblifuturebackend.bliblimart.command.GetUserCartCommand;
import com.bliblifuturebackend.bliblimart.command.UpdateCartQtyCommand;
import com.bliblifuturebackend.bliblimart.constant.ApiConstant;
import com.bliblifuturebackend.bliblimart.model.request.CartWishlistRequest;
import com.bliblifuturebackend.bliblimart.model.request.ListIdRequest;
import com.bliblifuturebackend.bliblimart.model.response.CartWishlistResponse;
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
@RequestMapping(ApiConstant.API_CART)
public class CartController {

    @Autowired
    private CommandExecutor commandExecutor;

    @GetMapping(value = "/me")
    @PreAuthorize("hasRole('USER')")
    public Mono<Response<List<CartWishlistResponse>>> getMyCart(Principal principal){
        return commandExecutor.execute(GetUserCartCommand.class, principal.getName())
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public Mono<Response<MessageResponse>> addCart(@RequestBody CartWishlistRequest request, Principal principal){
        request.setRequester(principal.getName());
        return commandExecutor.execute(CreateCartCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PutMapping()
    @PreAuthorize("hasRole('USER')")
    public Mono<Response<MessageResponse>> updateQtyCart(@RequestBody CartWishlistRequest request, Principal principal){
        request.setRequester(principal.getName());
        return commandExecutor.execute(UpdateCartQtyCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('USER')")
    public Mono<Response<MessageResponse>> deleteCarts(@RequestBody ListIdRequest request, Principal principal){
        request.setRequester(principal.getName());
        return commandExecutor.execute(DeleteCartsCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

}
