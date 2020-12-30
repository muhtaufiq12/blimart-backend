package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.CreateWishlistCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Product;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.entity.Wishlist;
import com.bliblifuturebackend.bliblimart.model.request.CartWishlistRequest;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.repository.ProductRepository;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import com.bliblifuturebackend.bliblimart.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

@Service
public class CreateWishlistCommandImpl implements CreateWishlistCommand {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Mono<MessageResponse> execute(CartWishlistRequest request) {
        return userRepository.findByUsername(request.getRequester())
                .flatMap(user -> wishlistRepository.findByProductIdAndUserId(request.getProductId(), user.getId())
                        .switchIfEmpty(Mono.just(Wishlist.builder().productId("0").build()))
                        .flatMap(wishlist -> productRepository.findById(request.getProductId())
                                        .doOnSuccess(this::checkNull)
                                        .flatMap(product -> saveWishlist(wishlist, request, user))
                        )
                );
    }

    private Mono<MessageResponse> saveWishlist(Wishlist res, CartWishlistRequest request, User user) {
        if (res.getProductId().equals("0")){
            Wishlist wishlist = createWishlist(request);
            wishlist.setUserId(user.getId());
            return wishlistRepository.save(wishlist)
                    .map(result -> MessageResponse.SUCCESS);
        }
        return Mono.just(MessageResponse.SUCCESS);
    }

    private void checkNull(Product product) {
        if (product == null){
            throw new NullPointerException("Product not found!");
        }
    }

    private Wishlist createWishlist(CartWishlistRequest request) {
        Wishlist wishlist = Wishlist.builder().productId(request.getProductId()).build();
        wishlist.setId(UUID.randomUUID().toString());

        String username = request.getRequester();

        Date date = new Date();
        wishlist.setCreatedDate(date);
        wishlist.setCreatedBy(username);
        wishlist.setUpdatedDate(date);
        wishlist.setUpdatedBy(username);

        return wishlist;
    }
}
