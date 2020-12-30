package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.CreateCartCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Cart;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.request.CartWishlistRequest;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.repository.CartRepository;
import com.bliblifuturebackend.bliblimart.repository.ProductRepository;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

@Service
public class CreateCartCommandImpl implements CreateCartCommand {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Mono<MessageResponse> execute(CartWishlistRequest request) {
        return userRepository.findByUsername(request.getRequester())
                .flatMap(user -> cartRepository.findByUserIdAndProductId(user.getId(), request.getProductId())
                        .switchIfEmpty(Mono.just(createCart(request, user)))
                        .map(cart -> {
                            cart.setTotalItem(cart.getTotalItem() + request.getTotalItem());
                            Date date = new Date();
                            cart.setUpdatedDate(date);
                            cart.setUpdatedBy(request.getRequester());
                            return cart;
                        })
                        .flatMap(this::checkStock)
                )
                .flatMap(cart -> cartRepository.save(cart))
                .map(cart -> MessageResponse.SUCCESS);
    }

    private Mono<Cart> checkStock(Cart cart) {
        return productRepository.findById(cart.getProductId())
                .doOnSuccess(product -> isAvailable(cart.getTotalItem(), product.getStock()))
                .map(product -> cart);
    }

    private void isAvailable(long totalItem, long stock) {
        if (totalItem > stock){
            throw new IllegalArgumentException("Insufficient stock!");
        }
    }

    private Cart createCart(CartWishlistRequest request, User user) {
        Cart cart = Cart.builder().productId(request.getProductId()).userId(user.getId()).build();
        cart.setId(UUID.randomUUID().toString());

        String username = request.getRequester();

        Date date = new Date();
        cart.setCreatedDate(date);
        cart.setCreatedBy(username);
        cart.setUpdatedDate(date);
        cart.setUpdatedBy(username);

        return cart;
    }
}
