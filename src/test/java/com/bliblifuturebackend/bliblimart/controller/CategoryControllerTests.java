package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.paging.Paging;
import com.blibli.oss.common.response.Response;
import com.bliblifuturebackend.bliblimart.command.*;
import com.bliblifuturebackend.bliblimart.model.entity.Category;
import com.bliblifuturebackend.bliblimart.model.request.*;
import com.bliblifuturebackend.bliblimart.model.response.CategoryResponse;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.model.response.PagingResponse;
import com.bliblifuturebackend.bliblimart.model.response.ProductResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = CategoryController.class)
public class CategoryControllerTests {

    @MockBean
    private CommandExecutor commandExecutor;

    @Autowired
    private CategoryController categoryController;


    private String categoryId1, categoryId2;

    private Category category1, category2;

    private CategoryResponse categoryResponse1, categoryResponse2;

    private Principal principal1;

    @Before
    public void setup() throws ParseException {

        categoryId1 = "categoryId1";
        categoryId2 = "categoryId2";

        category1 = Category.builder()
                .name("category1")
                .build();
        category1.setId(categoryId1);

        category2 = Category.builder()
                .name("category2")
                .build();
        category2.setId(categoryId2);


        categoryResponse1 = category1.createResponse();

        categoryResponse2 = category2.createResponse();

        principal1 = new Principal() {
            @Override
            public String getName() {
                return "username";
            }
        };

    }

    @Test
    public void test_getAll() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        List<CategoryResponse> data = Arrays.asList(categoryResponse1, categoryResponse2);
        PagingRequest request = PagingRequest.builder().page(0).size(100).build();

        Mono<List<CategoryResponse>> response = Mono.just(data);

        Mockito.when(commandExecutor.execute(GetAllCategoriesCommand.class, ""))
                .thenReturn(response);

        Response<List<CategoryResponse>> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        categoryController.getAllCategory()
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetAllCategoriesCommand.class, "");
    }

    @Test
    public void test_addProduct() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        CategoryRequest categoryRequest = CategoryRequest.builder()
                .name("category_name")
                .build();
        categoryRequest.setRequester(principal1.getName());


        Mono<CategoryResponse> response = Mono.just(categoryResponse1);
        Mockito.when(commandExecutor.execute(CreateCategoryCommand.class, categoryRequest))
                .thenReturn(response);

        Response<CategoryResponse> expected = new Response<>();
        expected.setData(categoryResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        categoryController.addCategory(categoryRequest,principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(CreateCategoryCommand.class, categoryRequest);
    }
    @Test
    public void test_updateProduct() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        CategoryRequest categoryRequest = CategoryRequest.builder()
                .name("category_name")
                .build();
        categoryRequest.setRequester(principal1.getName());


        Mono<CategoryResponse> response = Mono.just(categoryResponse1);
        Mockito.when(commandExecutor.execute(UpdateCategoryCommand.class, categoryRequest))
                .thenReturn(response);

        Response<CategoryResponse> expected = new Response<>();
        expected.setData(categoryResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        categoryController.updateCategory(categoryRequest,principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(UpdateCategoryCommand.class, categoryRequest);
    }
}
