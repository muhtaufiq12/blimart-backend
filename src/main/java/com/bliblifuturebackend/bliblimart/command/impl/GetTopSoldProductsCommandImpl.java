package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetTopSoldProductsCommand;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
import com.bliblifuturebackend.bliblimart.model.response.ProductResponse;
import com.bliblifuturebackend.bliblimart.model.response.responseUtil.ProductResponseUtil;
import com.bliblifuturebackend.bliblimart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GetTopSoldProductsCommandImpl implements GetTopSoldProductsCommand {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductResponseUtil productResponseUtil;

    @Override
    public Mono<List<ProductResponse>> execute(PagingRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return productRepository.findByTotalSoldGreaterThanAndBlimartIdOrderByTotalSoldDesc(-1, request.getBlimartId(), pageable)
                .flatMap(product -> productResponseUtil.getResponse(product))
                .collectList();
    }
}
