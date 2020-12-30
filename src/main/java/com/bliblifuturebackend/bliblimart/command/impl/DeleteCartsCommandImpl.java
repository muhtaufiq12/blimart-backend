package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.DeleteCartsCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Cart;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.request.ListIdRequest;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.repository.CartRepository;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class DeleteCartsCommandImpl implements DeleteCartsCommand {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<MessageResponse> execute(ListIdRequest request) {
        return userRepository.findByUsername(request.getRequester())
                .flatMap(user -> checkEachCartAndCreatedDeletedData(user, request.getIds())
                        .doOnNext(id -> cartRepository.deleteById(id).subscribe())
                        .collectList()
                )
                .map(list -> MessageResponse.SUCCESS);

    }

    private Flux<String> checkEachCartAndCreatedDeletedData(User user, List<String> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> cartRepository.findById(id)
                        .doOnSuccess(cart -> {
                            checkNull(cart);
                            verify(user, cart);
                        })
                        .map(cart -> id)
                );
    }

    private void verify(User user, Cart cart) {
        if (!user.getId().equals(cart.getUserId())){
            throw new SecurityException("Unauthorized");
        }
    }

    private void checkNull(Cart cart) {
        if (cart == null){
            throw new NullPointerException("Cart not found!");
        }
    }

}
