package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.CreateCategoryCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Category;
import com.bliblifuturebackend.bliblimart.model.request.CategoryRequest;
import com.bliblifuturebackend.bliblimart.model.response.CategoryResponse;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.repository.CategoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

@Service
public class CreateCategoryCommandImpl implements CreateCategoryCommand {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public Mono<CategoryResponse> execute(CategoryRequest request) {
        String cacheName = "com.bliblifuturebackend.bliblimart.model.entity.category";
        return categoryRepository.findByName(request.getName())
                .doOnSuccess(this::checkIfExists)
                .switchIfEmpty(Mono.just(createCategory(request)))
                .flatMap(category -> categoryRepository.save(category))
                .doOnSuccess(category -> redisTemplate.delete(cacheName))
                .map(Category::createResponse);
    }

    private void checkIfExists(Category category) {
        if (category != null){
            throw new IllegalArgumentException("Category with the same name already exists!");
        }
    }

    private Category createCategory(CategoryRequest request) {
        Category category = new Category();
        BeanUtils.copyProperties(request, category);
        category.setId(UUID.randomUUID().toString());

        String username = request.getRequester();

        Date date = new Date();
        category.setCreatedDate(date);
        category.setCreatedBy(username);
        category.setUpdatedDate(date);
        category.setUpdatedBy(username);

        return category;
    }
}
