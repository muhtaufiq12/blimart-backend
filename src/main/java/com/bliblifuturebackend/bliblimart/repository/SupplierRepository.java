package com.bliblifuturebackend.bliblimart.repository;

import com.bliblifuturebackend.bliblimart.model.entity.Supplier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SupplierRepository extends ReactiveMongoRepository<Supplier, String> {

    @Query("{ id: { $exists: true }}")
    Flux<Supplier> findAll(final Pageable pageable);

    Mono<Supplier> findByName(String name);

}
