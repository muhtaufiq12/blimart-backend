package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.CreateTransactionCommand;
import com.bliblifuturebackend.bliblimart.constant.TransactionConstant;
import com.bliblifuturebackend.bliblimart.model.entity.Transaction;
import com.bliblifuturebackend.bliblimart.model.entity.TransactionDetail;
import com.bliblifuturebackend.bliblimart.model.request.ProductTransactionRequest;
import com.bliblifuturebackend.bliblimart.model.request.TransactionRequest;
import com.bliblifuturebackend.bliblimart.model.response.TransactionDetailResponse;
import com.bliblifuturebackend.bliblimart.model.response.TransactionResponse;
import com.bliblifuturebackend.bliblimart.model.response.UserResponse;
import com.bliblifuturebackend.bliblimart.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

@Service
public class CreateTransactionCommandImpl implements CreateTransactionCommand {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private TransactionDetailRepository transactionDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Mono<TransactionResponse> execute(TransactionRequest request) {
        UserResponse userResponse = new UserResponse();
        return Mono.fromCallable(() -> createTansaction(request))
                .flatMap(transaction -> userRepository.findByUsername(request.getRequester())
                        .map(user -> {
                            BeanUtils.copyProperties(user.createResponse(), userResponse);
                            transaction.setUserId(user.getId());
                            return transaction;
                        }))
                .flatMap(transaction -> transactionRepository.save(transaction))
                .flatMap(transaction -> {
                    TransactionResponse response = transaction.createResponse();
                    response.setUser(userResponse);
                    return Flux.fromIterable(request.getProducts())
                            .map(productTransactionRequest -> createTransactionDetail(transaction, productTransactionRequest))
                            .flatMap(transactionDetail -> productRepository.findById(transactionDetail.getProductId())
                                    .flatMap(product -> {
                                        transactionDetail.setSubTotal(product.getPrice()*transactionDetail.getTotalItem());
                                        return transactionDetailRepository.save(transactionDetail)
                                                .doOnSuccess(detail -> cartRepository
                                                        .deleteByUserIdAndProductId(detail.getUserId(), detail.getProductId())
                                                        .subscribe()
                                                )
                                                .map(detail -> {
                                                    TransactionDetailResponse detailResponse = detail.createResponse();
                                                    detailResponse.setProductResponse(product.createResponse());
                                                    return detailResponse;
                                                });
                                    }))
                            .collectList()
                            .map(transactionDetailResponses -> {
                                response.setDetailResponses(transactionDetailResponses);
                                transactionDetailResponses.forEach(transactionDetailResponse -> transaction
                                        .setTotal(transaction.getTotal() + transactionDetailResponse.getSubTotal()));
                                response.setTotal(transaction.getTotal());
                                return response;
                            })
                            .doOnNext(transactionResponse -> transactionRepository.save(transaction).subscribe());
                });
    }

    private TransactionDetail createTransactionDetail(Transaction transaction, ProductTransactionRequest productTransactionRequest) {
        TransactionDetail transactionDetail = TransactionDetail.builder()
                .productId(productTransactionRequest.getProductId())
                .totalItem(productTransactionRequest.getTotalItem())
                .transactionId(transaction.getId())
                .build();

        String uuid = UUID.randomUUID().toString();
        transactionDetail.setId(uuid);
        transactionDetail.setTransactionDetailNumber(uuid.replace("-", "").toUpperCase());
        transactionDetail.setCreatedDate(transaction.getCreatedDate());
        transactionDetail.setCreatedBy(transaction.getCreatedBy());
        transactionDetail.setUpdatedDate(transaction.getUpdatedDate());
        transactionDetail.setUpdatedBy(transaction.getUpdatedBy());

        transactionDetail.setStatus(transaction.getStatus());
        transactionDetail.setUserId(transaction.getUserId());

        return transactionDetail;
    }

    private Transaction createTansaction(TransactionRequest request) {
        Transaction transaction = Transaction.builder()
                .status(TransactionConstant.WAITING_FOR_PAYMENT)
                .build();
        String uuid = UUID.randomUUID().toString();
        transaction.setId(uuid);
        transaction.setTransactionNumber(uuid.replace("-", "").toUpperCase());

        String username = request.getRequester();

        Date date = new Date();
        transaction.setCreatedDate(date);
        transaction.setCreatedBy(username);
        transaction.setUpdatedDate(date);
        transaction.setUpdatedBy(username);

        return transaction;
    }
}
