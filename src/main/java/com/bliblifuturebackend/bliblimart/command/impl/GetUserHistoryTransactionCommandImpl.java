package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetUserHistoryTransactionCommand;
import com.bliblifuturebackend.bliblimart.constant.TransactionConstant;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
import com.bliblifuturebackend.bliblimart.model.response.PagingResponse;
import com.bliblifuturebackend.bliblimart.model.response.TransactionDetailResponse;
import com.bliblifuturebackend.bliblimart.model.response.responseUtil.ProductResponseUtil;
import com.bliblifuturebackend.bliblimart.repository.ProductRepository;
import com.bliblifuturebackend.bliblimart.repository.TransactionDetailRepository;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetUserHistoryTransactionCommandImpl implements GetUserHistoryTransactionCommand {

    @Autowired
    private TransactionDetailRepository transactionDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductResponseUtil productResponseUtil;

    @Override
    public Mono<PagingResponse<TransactionDetailResponse>> execute(PagingRequest request) {
        PagingResponse<TransactionDetailResponse> response = new PagingResponse<>();
        return userRepository.findByUsername(request.getRequester())
                .flatMap(user -> transactionDetailRepository
                        .findByUserIdAndStatusOrStatusOrderByCreatedDateDesc(user.getId(), TransactionConstant.DONE, TransactionConstant.CANCELLED)
                        .flatMap(transactionDetail -> {
                            TransactionDetailResponse detailResponse = transactionDetail.createResponse();
                            return productRepository.findById(transactionDetail.getProductId())
                                    .flatMap(product -> productResponseUtil.getResponse(product))
                                    .map(productResponse -> {
                                        detailResponse.setProductResponse(productResponse);
                                        return detailResponse;
                                    });
                        })
                        .collectList()
                        .flatMap(transactionDetailResponses -> {
                            response.setData(transactionDetailResponses);
                            return transactionDetailRepository.countByUserIdAndStatusOrStatus(user.getId(), TransactionConstant.DONE, TransactionConstant.CANCELLED);
                        })
                        .map(total -> response.getPagingResponse(request, total.intValue()))
                );
    }
}
