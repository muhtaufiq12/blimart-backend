package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetProductCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Product;
import com.bliblifuturebackend.bliblimart.model.response.ProductResponse;
import com.bliblifuturebackend.bliblimart.model.response.responseUtil.ProductResponseUtil;
import com.bliblifuturebackend.bliblimart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetProductCommandImpl implements GetProductCommand {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductResponseUtil responseUtil;

    @Override
    public Mono<ProductResponse> execute(String id) {
        return productRepository.findById(id)
                .doOnSuccess(this::checkNull)
                .flatMap(product -> responseUtil.getResponse(product));
    }

    private void checkNull(Product product) {
        if (product == null){
            throw new NullPointerException("Product not found!");
        }
    }

}
