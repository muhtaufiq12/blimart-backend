package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.DeleteWishlistCommand;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.entity.Wishlist;
import com.bliblifuturebackend.bliblimart.model.request.CartWishlistRequest;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import com.bliblifuturebackend.bliblimart.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteWishlistCommandImpl implements DeleteWishlistCommand {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<MessageResponse> execute(CartWishlistRequest request) {
        return wishlistRepository.findById(request.getId())
                .doOnSuccess(this::checkNull)
                .flatMap(wishlist -> userRepository.findByUsername(request.getRequester())
                        .doOnSuccess(user -> verify(wishlist, user))
                        .map(user -> wishlistRepository.deleteById(request.getId()).subscribe())
                        .map(aVoid -> MessageResponse.SUCCESS)
                );
    }

    private void verify(Wishlist wishlist, User user) {
        if (!wishlist.getUserId().equals(user.getId())){
            throw new IllegalArgumentException("Unauthorized");
        }
    }

    private void checkNull(Wishlist wishlist) {
        if (wishlist == null){
            throw new NullPointerException();
        }
    }

}
