package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetUserCartCommand;
import com.bliblifuturebackend.bliblimart.model.response.CartWishlistResponse;
import com.bliblifuturebackend.bliblimart.model.response.responseUtil.ProductResponseUtil;
import com.bliblifuturebackend.bliblimart.repository.CartRepository;
import com.bliblifuturebackend.bliblimart.repository.ProductRepository;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GetUserCartCommandImpl implements GetUserCartCommand {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductResponseUtil productResponseUtil;

    @Override
    public Mono<List<CartWishlistResponse>> execute(String username) {
        return Mono.fromCallable(() -> username)
                .flatMap(uname -> userRepository.findByUsername(uname))
                .flatMap(user -> cartRepository.findByUserIdOrderByCreatedDateDesc(user.getId())
                        .flatMap(cart -> productRepository.findById(cart.getProductId())
                                .flatMap(product -> productResponseUtil.getResponse(product))
                                .map(productResponse -> {
                                    CartWishlistResponse data = cart.createResponse();
                                    data.setProductResponse(productResponse);
                                    return data;
                                })).collectList()
                )
                .map(cartWishlistResponses -> {
                    return cartWishlistResponses;
                });
    }

}
