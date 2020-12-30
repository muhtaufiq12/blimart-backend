package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.UpdateProductCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Product;
import com.bliblifuturebackend.bliblimart.model.request.ProductRequest;
import com.bliblifuturebackend.bliblimart.model.response.ProductResponse;
import com.bliblifuturebackend.bliblimart.model.response.responseUtil.ProductResponseUtil;
import com.bliblifuturebackend.bliblimart.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class UpdateProductCommandImpl implements UpdateProductCommand {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductResponseUtil responseUtil;

    @Override
    public Mono<ProductResponse> execute(ProductRequest request) {
        return productRepository.findById(request.getId())
                .doOnSuccess(this::checkNull)
                .map(product -> {
                    checkMark(request);
                    return updateProduct(request, product);
                })
                .flatMap(product -> productRepository.save(product))
                .flatMap(product -> responseUtil.getResponse(product));
    }

    private void checkNull(Product product) {
        if (product == null){
            throw new NullPointerException("Product not exists!");
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
            throw new IllegalArgumentException("Wrong mark input!");
        }
    }


    private Product updateProduct(ProductRequest request, Product product) {
        BeanUtils.copyProperties(request, product);

        long discount = request.getDiscount();
        long substraction = 0;
        if (discount > 0){
            substraction = product.getPrice() * discount/100;
        }
        product.setDiscountPrice(product.getPrice() - substraction);

        String username = request.getRequester();
        Date date = new Date();
        product.setUpdatedDate(date);
        product.setUpdatedBy(username);
        return product;
    }
}
