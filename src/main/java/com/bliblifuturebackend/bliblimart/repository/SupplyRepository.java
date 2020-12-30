package com.bliblifuturebackend.bliblimart.repository;

import com.bliblifuturebackend.bliblimart.model.entity.Supply;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SupplyRepository extends ReactiveMongoRepository<Supply, String> {

    @Query("{ id: { $exists: true }}")
    Flux<Supply> findAll(final Pageable pageable);

}
