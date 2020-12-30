package com.bliblifuturebackend.bliblimart.repository;

import com.bliblifuturebackend.bliblimart.model.entity.SupplierDeleted;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SupplierDeletedRepository extends ReactiveMongoRepository<SupplierDeleted, String> {

    @Query("{ id: { $exists: true }}")
    Flux<SupplierDeleted> findAll(final Pageable pageable);

}
