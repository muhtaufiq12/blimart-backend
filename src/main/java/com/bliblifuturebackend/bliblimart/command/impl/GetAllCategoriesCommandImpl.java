package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetAllCategoriesCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Category;
import com.bliblifuturebackend.bliblimart.model.response.CategoryResponse;
import com.bliblifuturebackend.bliblimart.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class GetAllCategoriesCommandImpl implements GetAllCategoriesCommand {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public Mono<List<CategoryResponse>> execute(String request) {
        String cacheName = "com.bliblifuturebackend.bliblimart.model.entity.category";
        List<Category> responses = findCategoryInCache(cacheName);
        if (!Objects.isNull(responses)) {
            return Flux.fromIterable(responses)
                    .map(Category::createResponse)
                    .collectList();
        }

        return categoryRepository.findAll()
                .collectList()
                .map(categoryList -> {
                    redisTemplate.opsForValue().set(cacheName, categoryList, 10, TimeUnit.HOURS);
                    return categoryList;
                })
                .flatMap(categoryList -> Flux.fromIterable(categoryList)
                        .map(Category::createResponse).collectList());
    }

    private List<Category> findCategoryInCache(String key) {
        if (redisTemplate.hasKey(key)) {
            return (List<Category>) this.redisTemplate.opsForValue().get(key);
        }
        return null;
    }
}
