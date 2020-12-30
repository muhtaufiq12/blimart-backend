package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.bliblifuturebackend.bliblimart.command.CreateWishlistCommand;
import com.bliblifuturebackend.bliblimart.command.DeleteWishlistCommand;
import com.bliblifuturebackend.bliblimart.command.GetUserWishlistCommand;
import com.bliblifuturebackend.bliblimart.constant.ApiConstant;
import com.bliblifuturebackend.bliblimart.model.request.CartWishlistRequest;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
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
@RequestMapping(ApiConstant.API_WISHLIST)
public class WishlistController {

    @Autowired
    private CommandExecutor commandExecutor;

    @GetMapping(value = "/me")
    @PreAuthorize("hasRole('USER')")
    public Mono<Response<List<CartWishlistResponse>>> getMyWishlist(@RequestParam("page") int page, @RequestParam("size") int size, Principal principal){
        PagingRequest request = PagingRequest.builder().page(page-1).size(size).build();
        request.setRequester(principal.getName());
        return commandExecutor.execute(GetUserWishlistCommand.class, request)
                .map(pagingResponse -> {
                    Response<List<CartWishlistResponse>> response = ResponseHelper.ok(pagingResponse.getData());
                    response.setPaging(pagingResponse.getPaging());
                    return response;
                })
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public Mono<Response<MessageResponse>> addWishlist(@RequestBody CartWishlistRequest request, Principal principal){
        request.setRequester(principal.getName());
        return commandExecutor.execute(CreateWishlistCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public Mono<Response<MessageResponse>> deleteWishlist(@PathVariable String id, Principal principal){
        CartWishlistRequest request = CartWishlistRequest.builder().productId("0").build();
        request.setId(id);
        request.setRequester(principal.getName());
        return commandExecutor.execute(DeleteWishlistCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

}
