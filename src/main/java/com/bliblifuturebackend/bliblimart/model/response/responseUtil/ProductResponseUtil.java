package com.bliblifuturebackend.bliblimart.model.response.responseUtil;

import com.bliblifuturebackend.bliblimart.model.entity.Product;
import com.bliblifuturebackend.bliblimart.model.response.ProductResponse;
import com.bliblifuturebackend.bliblimart.repository.BlimartRepository;
import com.bliblifuturebackend.bliblimart.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ProductResponseUtil {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BlimartRepository blimartRepository;

    public Mono<ProductResponse> getResponse(Product product){
        ProductResponse response = product.createResponse();
        return categoryRepository.findById(product.getCategoryId())
                .flatMap(category -> {
                    response.setCategory(category.getName());
                    return blimartRepository.findById(product.getBlimartId());
                })
                .map(blimart -> {
                    response.setBlimartResponse(blimart.createResponse());
                    return response;
                });
    }
}
