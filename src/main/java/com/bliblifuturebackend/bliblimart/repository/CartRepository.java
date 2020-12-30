package com.bliblifuturebackend.bliblimart.repository;

import com.bliblifuturebackend.bliblimart.model.entity.Cart;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CartRepository extends ReactiveMongoRepository<Cart, String> {

    Flux<Cart> findByUserIdOrderByCreatedDateDesc(String userId);

    Mono<Cart> findByIdAndUserId(String id, String userId);

    Mono<Cart> findByUserIdAndProductId(String userId, String productId);

    Mono<Void> deleteByUserIdAndProductId(String userId, String productId);

}
