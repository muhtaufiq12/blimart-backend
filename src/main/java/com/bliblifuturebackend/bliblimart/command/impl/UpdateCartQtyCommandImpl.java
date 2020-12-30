package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.UpdateCartQtyCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Cart;
import com.bliblifuturebackend.bliblimart.model.request.CartWishlistRequest;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.repository.CartRepository;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class UpdateCartQtyCommandImpl implements UpdateCartQtyCommand {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<MessageResponse> execute(CartWishlistRequest request) {
        return userRepository.findByUsername(request.getRequester())
                .flatMap(user -> cartRepository.findByIdAndUserId(request.getId(), user.getId())
                        .doOnSuccess(this::checkNull)
                        .flatMap(cart -> cartRepository.save(updateQtyCart(cart, request))
                                .doOnSuccess(this::checkToDelete)
                                .map(res -> MessageResponse.SUCCESS)
                        )
                );

    }

    private void checkToDelete(Cart cart) {
        if (cart.getTotalItem() == 0){
            cartRepository.deleteById(cart.getId()).subscribe();
        }
    }

    private void checkNull(Cart cart) {
        if (cart == null){
            throw new NullPointerException("Cart not found!");
        }
    }

    private Cart updateQtyCart(Cart cart, CartWishlistRequest request) {
        cart.setTotalItem(request.getTotalItem());

        Date date = new Date();
        cart.setUpdatedDate(date);
        cart.setUpdatedBy(request.getRequester());

        return cart;
    }

    private MessageResponse createResponse(){
        return MessageResponse.SUCCESS;
    }
}
