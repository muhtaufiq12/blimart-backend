package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetProductsByKeywordCommand;
import com.bliblifuturebackend.bliblimart.model.request.SearchRequest;
import com.bliblifuturebackend.bliblimart.model.response.PagingResponse;
import com.bliblifuturebackend.bliblimart.model.response.ProductResponse;
import com.bliblifuturebackend.bliblimart.model.response.responseUtil.ProductResponseUtil;
import com.bliblifuturebackend.bliblimart.repository.ProductElasticsearchRepository;
import com.bliblifuturebackend.bliblimart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetProductsByKeywordCommandImpl implements GetProductsByKeywordCommand {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductElasticsearchRepository productElasticsearchRepository;

    @Autowired
    private ProductResponseUtil productResponseUtil;

    @Override
    public Mono<PagingResponse<ProductResponse>> execute(SearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        PagingResponse<ProductResponse> response = new PagingResponse<>();

        return productElasticsearchRepository.search("*" + request.getKeyword() + "*", pageable)
                .flatMap(productIndex -> productRepository.findById(productIndex.getId()))
                .filter(product -> product.getBlimartId().equals(request.getBlimartId()))
                .flatMap(product -> productResponseUtil.getResponse(product))
                .collectList()
                .map(productResponses -> {
                    response.setData(productResponses);
                    return productResponses.size();
                })
                .map(total -> response.getPagingResponse(request, total));
    }
}
