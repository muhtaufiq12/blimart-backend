package com.bliblifuturebackend.bliblimart.model.response.responseUtil;

import com.bliblifuturebackend.bliblimart.model.entity.Transaction;
import com.bliblifuturebackend.bliblimart.model.response.TransactionDetailResponse;
import com.bliblifuturebackend.bliblimart.model.response.TransactionResponse;
import com.bliblifuturebackend.bliblimart.repository.ProductRepository;
import com.bliblifuturebackend.bliblimart.repository.TransactionDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TransactionResponseUtil {

    @Autowired
    private TransactionDetailRepository transactionDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductResponseUtil productResponseUtil;

    public Mono<TransactionResponse> getTransactionResponseWithDetail(Transaction transaction) {
        TransactionResponse transactionResponse = transaction.createResponse();
        return transactionDetailRepository.findByTransactionId(transactionResponse.getId())
                .flatMap(transactionDetail -> {
                    TransactionDetailResponse subResponse = transactionDetail.createResponse();
                    return productRepository.findById(transactionDetail.getProductId())
                            .flatMap(product -> productResponseUtil.getResponse(product)
                                    .map(productResponse -> {
                                        subResponse.setProductResponse(productResponse);
                                        return subResponse;
                                    })
                            );
                })
                .collectList()
                .map(transactionDetailResponses -> {
                    transactionResponse.setDetailResponses(transactionDetailResponses);
                    return transactionResponse;
                });
    }
}
