package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetUserWishlistCommand;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
import com.bliblifuturebackend.bliblimart.model.response.CartWishlistResponse;
import com.bliblifuturebackend.bliblimart.model.response.PagingResponse;
import com.bliblifuturebackend.bliblimart.model.response.responseUtil.ProductResponseUtil;
import com.bliblifuturebackend.bliblimart.repository.ProductRepository;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import com.bliblifuturebackend.bliblimart.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetUserWishlistCommandImpl implements GetUserWishlistCommand {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductResponseUtil productResponseUtil;

    @Override
    public Mono<PagingResponse<CartWishlistResponse>> execute(PagingRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        PagingResponse<CartWishlistResponse> response = new PagingResponse<>();
        return Mono.fromCallable(request::getRequester)
                .flatMap(username -> userRepository.findByUsername(username))
                .flatMap(user -> wishlistRepository.findByUserIdOrderByCreatedDateDesc(user.getId(), pageable)
                        .flatMap(wishlist -> productRepository.findById(wishlist.getProductId())
                                .flatMap(product -> productResponseUtil.getResponse(product))
                                .map(productResponse -> {
                                    CartWishlistResponse data = wishlist.createResponse();
                                    data.setProductResponse(productResponse);
                                    return data;
                                })).collectList())
                .flatMap(cartWishlistResponses -> {
                    response.setData(cartWishlistResponses);
                    return productRepository.count();
                })
                .map(total -> response.getPagingResponse(request, total.intValue()));
    }
}
