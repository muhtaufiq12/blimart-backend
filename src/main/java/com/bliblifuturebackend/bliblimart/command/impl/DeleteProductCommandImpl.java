package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.DeleteProductCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Product;
import com.bliblifuturebackend.bliblimart.model.entity.ProductDeleted;
import com.bliblifuturebackend.bliblimart.model.request.BaseRequest;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.repository.ProductDeletedRepository;
import com.bliblifuturebackend.bliblimart.repository.ProductElasticsearchRepository;
import com.bliblifuturebackend.bliblimart.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class DeleteProductCommandImpl implements DeleteProductCommand {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDeletedRepository productDeletedRepository;

    @Autowired
    private ProductElasticsearchRepository productElasticsearchRepository;

    @Override
    public Mono<MessageResponse> execute(BaseRequest request) {
        return productRepository.findById(request.getId())
                .doOnSuccess(this::checkNullAndDelete)
                .map(product -> createDeletedProduct(product, request.getRequester()))
                .flatMap(productDeleted -> productDeletedRepository.save(productDeleted))
                .map(productDeleted -> MessageResponse.SUCCESS);
    }

    private ProductDeleted createDeletedProduct(Product product, String username) {
        ProductDeleted productDeleted = new ProductDeleted();
        BeanUtils.copyProperties(product, productDeleted);
        productDeleted.setDeletedBy(username);
        productDeleted.setDeletedDate(new Date());
        return productDeleted;
    }

    private void checkNullAndDelete(Product product) {
        if (product == null){
            throw new NullPointerException("Product not found!");
        }

        productRepository.deleteById(product.getId()).subscribe();
        productElasticsearchRepository.deleteById(product.getId()).subscribe();
    }

}
