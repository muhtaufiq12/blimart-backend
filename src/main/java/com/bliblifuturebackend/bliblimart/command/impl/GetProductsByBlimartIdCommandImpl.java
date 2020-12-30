package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetProductsByBlimartIdCommand;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
import com.bliblifuturebackend.bliblimart.model.response.PagingResponse;
import com.bliblifuturebackend.bliblimart.model.response.ProductResponse;
import com.bliblifuturebackend.bliblimart.model.response.responseUtil.ProductResponseUtil;
import com.bliblifuturebackend.bliblimart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetProductsByBlimartIdCommandImpl implements GetProductsByBlimartIdCommand {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductResponseUtil productResponseUtil;

    @Override
    public Mono<PagingResponse<ProductResponse>> execute(PagingRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        PagingResponse<ProductResponse> response = new PagingResponse<>();
        return productRepository.findByBlimartIdOrderByCreatedDate(request.getBlimartId(), pageable)
                .flatMap(product -> productResponseUtil.getResponse(product))
                .collectList()
                .flatMap(productResponses -> {
                    response.setData(productResponses);
                    return productRepository.countByBlimartId(request.getBlimartId());
                })
                .map(total -> response.getPagingResponse(request, Math.toIntExact(total)));
    }
}
