package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetRandomByRelatedProductsCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Product;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
import com.bliblifuturebackend.bliblimart.model.response.ProductResponse;
import com.bliblifuturebackend.bliblimart.model.response.responseUtil.ProductResponseUtil;
import com.bliblifuturebackend.bliblimart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

@Service
public class GetRandomByRelatedProductsCommandImpl implements GetRandomByRelatedProductsCommand {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductResponseUtil productResponseUtil;

    @Override
    public Mono<List<ProductResponse>> execute(PagingRequest request) {
        return productRepository.count()
                .flatMap(total -> productRepository.findById(request.getId())
                        .flatMap(relatedProduct -> getRandomByRelatedProducts(relatedProduct, total, request.getSize(), request.getBlimartId())
                                .flatMap(product -> productResponseUtil.getResponse(product))
                                .collectList()
                        )
                );
    }

    private Flux<Product> getRandomByRelatedProducts(Product product, Long total, int size, String blimartId) {
        int totalPage = (int) Math.ceil(total/(float)size);
        int randomPage = new Random().nextInt(totalPage);

        Pageable pageable = PageRequest.of(randomPage, size);

        return productRepository.findByCategoryIdAndBlimartIdAndIdNot(product.getCategoryId(), blimartId, product.getId(), pageable);
    }
}
