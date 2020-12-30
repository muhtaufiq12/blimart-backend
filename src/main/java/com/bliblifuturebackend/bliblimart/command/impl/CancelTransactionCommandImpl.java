package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.CancelTransactionCommand;
import com.bliblifuturebackend.bliblimart.constant.TransactionConstant;
import com.bliblifuturebackend.bliblimart.model.entity.Transaction;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.request.BaseRequest;
import com.bliblifuturebackend.bliblimart.model.response.TransactionResponse;
import com.bliblifuturebackend.bliblimart.model.response.responseUtil.TransactionResponseUtil;
import com.bliblifuturebackend.bliblimart.repository.TransactionDetailRepository;
import com.bliblifuturebackend.bliblimart.repository.TransactionRepository;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class CancelTransactionCommandImpl implements CancelTransactionCommand {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionDetailRepository transactionDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionResponseUtil transactionResponseUtil;

    @Override
    public Mono<TransactionResponse> execute(BaseRequest request) {
        return transactionRepository.findById(request.getId())
                .doOnSuccess(this::checkNull)
                .flatMap(transaction -> userRepository.findByUsername(request.getRequester())
                        .flatMap(user -> cancelProcess(transaction, user)))
                .flatMap(transaction -> transactionRepository.save(transaction)
                        .flatMap(res -> transactionResponseUtil.getTransactionResponseWithDetail(res))
                );
    }

    private void checkNull(Transaction transaction) {
        if (transaction == null){
            throw new NullPointerException("Transaction not found!");
        }
    }

    private Mono<Transaction> cancelProcess(Transaction transaction, User user){
        if (transaction.getUserId().equals(user.getId())){
            if (transaction.getStatus().equals(TransactionConstant.WAITING_FOR_PAYMENT)){
                transaction.setStatus(TransactionConstant.CANCELLED);
                transaction.setUpdatedBy(user.getUsername());
                transaction.setUpdatedDate(new Date());
                return transactionDetailRepository.findByTransactionId(transaction.getId())
                        .flatMap(transactionDetail -> {
                            transactionDetail.setStatus(transaction.getStatus());
                            transactionDetail.setUpdatedDate(transaction.getUpdatedDate());
                            transactionDetail.setUpdatedBy(transaction.getUpdatedBy());
                            return transactionDetailRepository.save(transactionDetail);
                        })
                        .collectList()
                        .map(transactionDetails -> transaction);
            }
            throw new IllegalArgumentException("Bad Request");
        }
        throw new SecurityException("Unauthorized");
    }
}
