package com.bliblifuturebackend.bliblimart.repository;

import com.bliblifuturebackend.bliblimart.model.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

    @Query("{ id: { $exists: true }}")
    Flux<Product> findAll(final Pageable pageable);

    Flux<Product> findByBlimartId(String blimartId, Pageable pageable);

    Mono<Product> findByNameAndBlimartId(String name, String blimartId);

    Flux<Product> findByTotalSoldGreaterThanAndBlimartIdOrderByTotalSoldDesc(int totalSold, String blimartId, Pageable pageable);

    Flux<Product> findById(String id, Pageable pageable);

    Flux<Product> findByDiscountGreaterThanAndBlimartIdOrderByDiscountDesc(int disc, String blimartId, Pageable pageable);

    Flux<Product> findByCategoryIdAndBlimartIdOrderByCreatedDate(String categoryId, String blimartId, Pageable pageable);

    Flux<Product> findByBlimartIdOrderByCreatedDate(String blimartId, Pageable pageable);

    Mono<Long> countByCategoryIdAndBlimartId(String categoryId, String blimartId);

    Mono<Long> countByBlimartId(String blimartId);

    Flux<Product> findByCategoryIdAndBlimartIdAndIdNot(String categoryId, String blimartId, String id, Pageable pageable);

}
