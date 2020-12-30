package com.bliblifuturebackend.bliblimart.repository;

import com.bliblifuturebackend.bliblimart.model.entity.Blimart;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BlimartRepository extends ReactiveMongoRepository<Blimart, String> {

    @Query("{ id: { $exists: true }}")
    Flux<Blimart> findAll(final Pageable pageable);

    Mono<Blimart> findByName(String name);

}
