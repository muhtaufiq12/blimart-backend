package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.PaymentCommand;
import com.bliblifuturebackend.bliblimart.constant.TransactionConstant;
import com.bliblifuturebackend.bliblimart.model.entity.Transaction;
import com.bliblifuturebackend.bliblimart.model.entity.TransactionDetail;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.request.PaymentRequest;
import com.bliblifuturebackend.bliblimart.model.response.TransactionResponse;
import com.bliblifuturebackend.bliblimart.model.response.responseUtil.TransactionResponseUtil;
import com.bliblifuturebackend.bliblimart.repository.ProductRepository;
import com.bliblifuturebackend.bliblimart.repository.TransactionDetailRepository;
import com.bliblifuturebackend.bliblimart.repository.TransactionRepository;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class PaymentCommandImpl implements PaymentCommand {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionDetailRepository transactionDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionResponseUtil transactionResponseUtil;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Mono<TransactionResponse> execute(PaymentRequest request) {
        return transactionRepository.findById(request.getTransactionId())
                .doOnSuccess(this::checkNull)
                .flatMap(transaction -> userRepository.findByUsername(request.getUsername())
                        .flatMap(user -> paymentProcess(transaction, user)))
                .flatMap(transaction -> transactionRepository.save(transaction)
                        .flatMap(res -> transactionResponseUtil.getTransactionResponseWithDetail(res))
                );
    }

    private void checkNull(Transaction transaction) {
        if (transaction == null){
            throw new NullPointerException("Transaction not found!");
        }
    }

    private Mono<Transaction> paymentProcess(Transaction transaction, User user){
        if (transaction.getUserId().equals(user.getId())){
            if (transaction.getStatus().equals(TransactionConstant.WAITING_FOR_PAYMENT)){
                transaction.setStatus(TransactionConstant.DONE);
                transaction.setUpdatedBy(user.getUsername());
                transaction.setUpdatedDate(new Date());
                return transactionDetailRepository.findByTransactionId(transaction.getId())
                        .map(transactionDetail -> {
                            transactionDetail.setStatus(transaction.getStatus());
                            transactionDetail.setUpdatedDate(transaction.getUpdatedDate());
                            transactionDetail.setUpdatedBy(transaction.getUpdatedBy());
                            return transactionDetail;
                        })
                        .flatMap(this::checkStockAndUpdate)
                        .flatMap(transactionDetail -> transactionDetailRepository.save(transactionDetail))
                        .collectList()
                        .map(transactionDetails -> transaction);
            }
            throw new IllegalArgumentException("Bad Request");
        }
        throw new SecurityException("Unauthorized");
    }

    private Mono<TransactionDetail> checkStockAndUpdate(TransactionDetail transactionDetail) {
        return productRepository.findById(transactionDetail.getProductId())
                .doOnSuccess(product -> isAvailable(transactionDetail.getTotalItem(), product.getStock()))
                .flatMap(product -> {
                    long totalItem = transactionDetail.getTotalItem();
                    product.setStock(product.getStock() - totalItem);
                    product.setTotalSold(product.getTotalSold() + totalItem);
                    return productRepository.save(product);
                })
                .map(product -> transactionDetail);
    }

    private void isAvailable(long totalItem, long stock) {
        if (totalItem > stock){
            throw new IllegalArgumentException("Insufficient stock!");
        }
    }

    private Mono<TransactionDetail> updateProduct(TransactionDetail detail) {
        return productRepository.findById(detail.getProductId())
                .flatMap(product -> {
                    long totalItem = detail.getTotalItem();
                    product.setStock(product.getStock() - totalItem);
                    product.setTotalSold(product.getTotalSold() + totalItem);
                    return productRepository.save(product);
                })
                .map(product -> detail);
    }
}
