package com.bliblifuturebackend.bliblimart.repository;

import com.bliblifuturebackend.bliblimart.model.entity.Mark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MarkRepository extends ReactiveMongoRepository<Mark, String> {

    @Query("{ id: { $exists: true }}")
    Flux<Mark> findAll(final Pageable pageable);

    Mono<Mark> findByName(String name);

}
