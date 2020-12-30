package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.CreateProductCommand;
import com.bliblifuturebackend.bliblimart.constant.ProductConstant;
import com.bliblifuturebackend.bliblimart.model.entity.Product;
import com.bliblifuturebackend.bliblimart.model.entity.ProductIndex;
import com.bliblifuturebackend.bliblimart.model.request.ProductRequest;
import com.bliblifuturebackend.bliblimart.model.response.ProductResponse;
import com.bliblifuturebackend.bliblimart.model.response.responseUtil.ProductResponseUtil;
import com.bliblifuturebackend.bliblimart.repository.ProductElasticsearchRepository;
import com.bliblifuturebackend.bliblimart.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

@Service
public class CreateProductCommandImpl implements CreateProductCommand {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductElasticsearchRepository productElasticsearchRepository;

    @Autowired
    private ProductResponseUtil responseUtil;

    @Override
    public Mono<ProductResponse> execute(ProductRequest request) {
        return productRepository.findByNameAndBlimartId(request.getName(), request.getBlimartId())
                .doOnSuccess(this::checkIfExists)
                .switchIfEmpty(Mono.just(createProduct(request)))
                .map(product -> {
                    checkMark(request);
                    return createProduct(request);
                })
                .flatMap(product -> productRepository.save(product).doOnSuccess(this::saveElasticsearch))
                .flatMap(product -> responseUtil.getResponse(product));
    }

    private void checkIfExists(Product product) {
        if (product != null){
            throw new IllegalArgumentException("Product with the same name already exists!");
        }
    }

    private void checkMark(ProductRequest request) {
        boolean isValid = false;
        String mark1 = request.getMark1().substring(1);
        String mark2 = request.getMark2().substring(1);
        String concat = mark1 + mark2;

        if (concat.equals("we") || concat.equals("ew")){
            isValid = true;
        }

        if (!isValid){
            throw new IllegalArgumentException("Unacceptable mark input!");
        }
    }

    private void saveElasticsearch(Product product) {
        ProductIndex productIndex = ProductIndex.builder().id(product.getId()).name(product.getName()).build();
        productElasticsearchRepository.save(productIndex).subscribe();
    }

    private Product createProduct(ProductRequest request) {
        Product product = new Product();
        BeanUtils.copyProperties(request, product);

        long discount = request.getDiscount();
        long substraction = 0;
        if (discount > 0){
            substraction = product.getPrice() * discount/100;
        }
        product.setDiscountPrice(product.getPrice() - substraction);

        String id = UUID.randomUUID().toString();
        product.setId(id);
        product.setPhotoUrl(ProductConstant.PHOTO_URL + id);

        String username = request.getRequester();

        Date date = new Date();
        product.setCreatedDate(date);
        product.setCreatedBy(username);
        product.setUpdatedDate(date);
        product.setUpdatedBy(username);

        return product;
    }
}
