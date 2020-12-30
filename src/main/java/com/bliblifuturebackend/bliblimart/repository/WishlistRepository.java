package com.bliblifuturebackend.bliblimart.repository;

import com.bliblifuturebackend.bliblimart.model.entity.Wishlist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface WishlistRepository extends ReactiveMongoRepository<Wishlist, String> {

    Flux<Wishlist> findByUserIdOrderByCreatedDateDesc(String userId, Pageable pageable);

    Mono<Wishlist> findByProductIdAndUserId(String productId, String userId);

}
