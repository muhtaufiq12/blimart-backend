package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetTransactionCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Transaction;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.request.BaseRequest;
import com.bliblifuturebackend.bliblimart.model.response.TransactionResponse;
import com.bliblifuturebackend.bliblimart.model.response.UserResponse;
import com.bliblifuturebackend.bliblimart.model.response.responseUtil.TransactionResponseUtil;
import com.bliblifuturebackend.bliblimart.repository.TransactionRepository;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetTransactionCommandImpl implements GetTransactionCommand {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionResponseUtil transactionResponseUtil;

    @Override
    public Mono<TransactionResponse> execute(BaseRequest request) {
        UserResponse userResponse = new UserResponse();
        return transactionRepository.findById(request.getId())
                .flatMap(transaction -> userRepository.findByUsername(request.getRequester())
                        .map(user -> {
                            BeanUtils.copyProperties(user.createResponse(), userResponse);
                            return verifyRequest(transaction, user);
                        }))
                .flatMap(transaction -> transactionResponseUtil.getTransactionResponseWithDetail(transaction))
                .map(transactionResponse -> {
                    transactionResponse.setUser(userResponse);
                    return transactionResponse;
                });
    }

    private Transaction verifyRequest(Transaction transaction, User user){
        if (!transaction.getUserId().equals(user.getId()) && !user.getIsAdmin()){
            throw new IllegalArgumentException("Unauthorized");
        }
        return transaction;
    }

}
