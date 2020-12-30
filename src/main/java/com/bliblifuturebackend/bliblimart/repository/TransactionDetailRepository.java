package com.bliblifuturebackend.bliblimart.repository;

import com.bliblifuturebackend.bliblimart.model.entity.TransactionDetail;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TransactionDetailRepository extends ReactiveMongoRepository<TransactionDetail, String> {

    Flux<TransactionDetail> findByTransactionId(String transactionId);

    Flux<TransactionDetail> findByUserIdAndStatusOrStatusOrderByCreatedDateDesc(String userId, String status, String status2);

    Mono<TransactionDetail> findFirstByTransactionId(String transactionId);

    Mono<Long> countByUserIdAndStatusOrStatus(String userId, String status, String status2);

}
