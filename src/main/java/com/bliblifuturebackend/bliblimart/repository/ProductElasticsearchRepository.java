package com.bliblifuturebackend.bliblimart.repository;

import com.bliblifuturebackend.bliblimart.model.entity.ProductIndex;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductElasticsearchRepository extends ReactiveCrudRepository<ProductIndex, String> {

    @Query("{\"bool\": {\"must\": {\"wildcard\": {\"name\": \"?0\"} } } }")
    Flux<ProductIndex> search(String value, Pageable pageable);

}
