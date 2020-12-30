package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.UpdateCategoryCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Category;
import com.bliblifuturebackend.bliblimart.model.request.CategoryRequest;
import com.bliblifuturebackend.bliblimart.model.response.CategoryResponse;
import com.bliblifuturebackend.bliblimart.repository.CategoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class UpdateCategoryCommandImpl implements UpdateCategoryCommand {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public Mono<CategoryResponse> execute(CategoryRequest request) {
        String cacheName = "com.bliblifuturebackend.bliblimart.model.entity.category";
        return Mono.fromCallable(request::getId)
                .flatMap(id -> categoryRepository.findById(id))
                .map(category -> updateCategory(request, category))
                .flatMap(category -> categoryRepository.save(category))
                .doOnSuccess(category -> redisTemplate.delete(cacheName))
                .map(category -> category.createResponse());
    }

    private Category updateCategory(CategoryRequest request, Category category) {
        BeanUtils.copyProperties(request, category);
        String username = request.getRequester();
        Date date = new Date();
        category.setUpdatedDate(date);
        category.setUpdatedBy(username);
        return category;
    }
}
