package com.bliblifuturebackend.bliblimart.repository;

import com.bliblifuturebackend.bliblimart.model.entity.ProductDeleted;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductDeletedRepository extends ReactiveMongoRepository<ProductDeleted, String> {

    @Query("{ id: { $exists: true }}")
    Flux<ProductDeleted> findAll(final Pageable pageable);

}
