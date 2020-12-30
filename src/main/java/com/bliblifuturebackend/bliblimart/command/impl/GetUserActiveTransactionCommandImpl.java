package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetUserActiveTransactionCommand;
import com.bliblifuturebackend.bliblimart.constant.TransactionConstant;
import com.bliblifuturebackend.bliblimart.model.response.TransactionResponse;
import com.bliblifuturebackend.bliblimart.model.response.responseUtil.TransactionResponseUtil;
import com.bliblifuturebackend.bliblimart.repository.TransactionRepository;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GetUserActiveTransactionCommandImpl implements GetUserActiveTransactionCommand {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionResponseUtil transactionResponseUtil;

    @Override
    public Mono<List<TransactionResponse>> execute(String username) {
        return userRepository.findByUsername(username)
                .flatMap(user -> transactionRepository.findByUserIdAndStatusOrderByCreatedDateDesc(user.getId(), TransactionConstant.WAITING_FOR_PAYMENT)
                        .flatMap(transaction -> transactionResponseUtil.getTransactionResponseWithDetail(transaction))
                        .collectList()
                );
    }
}
