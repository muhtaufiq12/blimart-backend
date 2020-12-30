package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.paging.Paging;
import com.blibli.oss.common.response.Response;
import com.bliblifuturebackend.bliblimart.command.*;
import com.bliblifuturebackend.bliblimart.model.entity.Product;
import com.bliblifuturebackend.bliblimart.model.request.*;
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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = ProductController.class)
public class ProductControllerTests {

    @MockBean
    private CommandExecutor commandExecutor;

    @Autowired
    private ProductController productController;

    String blimartId = "blimartId";
    String requestId = "blimartId";
    String dateString1, dateString2;


    private String productId1, productId2;

    private Product product1, product2;

    private ProductResponse productResponse1, productResponse2;

    private Principal principal1;

    @Before
    public void setup() throws ParseException {

        productId1 = "productId1";
        productId2 = "productId2";

        product1 = Product.builder()
                .name("product1")
                .blimartId("blimartId")
                .categoryId("categoryId")
                .mark1("Ae")
                .mark2("Bw")
                .price(1000)
                .photoUrl("/user-photo/" + productId1)
                .build();
        product1.setId(productId1);

        product2 = Product.builder()
                .name("product2")
                .blimartId("blimartId")
                .categoryId("categoryId")
                .mark1("Ae")
                .mark2("Bw")
                .price(2000)
                .discount(10)
                .stock(20)
                .photoUrl("/user-photo/" + productId1)
                .build();
        product2.setId(productId2);


        productResponse1 = product1.createResponse();

        productResponse2 = product2.createResponse();

        principal1 = new Principal() {
            @Override
            public String getName() {
                return "username";
            }
        };

        dateString1 = "2000-01-30";

        dateString2 = "2001-01-30";

        Date date1  = new SimpleDateFormat("yyyy-MM-dd").parse(dateString1);

        Date date2  = new SimpleDateFormat("yyyy-MM-dd").parse(dateString2);
    }

    @Test
    public void test_getProductId() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        Mono<ProductResponse> res = Mono.just(productResponse1);

        Mockito.when(commandExecutor.execute(GetProductCommand.class, productId1))
                .thenReturn(res);

        Response<ProductResponse> expected = new Response<>();
        expected.setData(productResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        productController.getProductById(productId1)
                .subscribe(response -> {
                    Assert.assertEquals(response.getCode(), expected.getCode());
                    Assert.assertEquals(response.getStatus(), expected.getStatus());
                    Assert.assertEquals(response.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetProductCommand.class, productId1);
    }
    @Test
    public void test_getAllProduct() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        List<ProductResponse> data = Arrays.asList(productResponse1, productResponse2);
        PagingResponse<ProductResponse> pagingResponse = new PagingResponse(
                data, Paging.builder().itemPerPage(100).page(1).totalItem(2).totalPage(1).build()
        );
        PagingRequest request = PagingRequest.builder().page(0).size(100).build();

        Mono<PagingResponse<ProductResponse>> response = Mono.just(pagingResponse);

        Mockito.when(commandExecutor.execute(GetAllProductsCommand.class, request))
                .thenReturn(response);

        Response<List<ProductResponse>> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        productController.getAllProducts(1, 100)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetAllProductsCommand.class, request);
    }

    @Test
    public void test_getProductsByBlimartId() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        List<ProductResponse> data = Arrays.asList(productResponse1, productResponse2);
        PagingResponse<ProductResponse> pagingResponse = new PagingResponse(
                data, Paging.builder().itemPerPage(100).page(1).totalItem(2).totalPage(1).build()
        );
        PagingRequest request = PagingRequest.builder().page(0).size(100).blimartId(blimartId).build();

        Mono<PagingResponse<ProductResponse>> response = Mono.just(pagingResponse);
        Mockito.when(commandExecutor.execute(GetProductsByBlimartIdCommand.class, request))
                .thenReturn(response);

        Response<List<ProductResponse>> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        productController.getProductsByBliblimartId(blimartId,1, 100)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetProductsByBlimartIdCommand.class, request);
    }
    @Test
    public void test_getProductsByCategoryId() {
        List<ProductResponse> data = Arrays.asList(productResponse1, productResponse2);
        PagingResponse<ProductResponse> pagingResponse = new PagingResponse(
                data, Paging.builder().itemPerPage(100).page(1).totalItem(2).totalPage(1).build()
        );
        PagingRequest request = PagingRequest.builder().page(0).size(100).blimartId(blimartId).build();
        request.setId(requestId);

        Mono<PagingResponse<ProductResponse>> response = Mono.just(pagingResponse);
        Mockito.when(commandExecutor.execute(GetProductsByCategoryIdCommand.class, request))
                .thenReturn(response);

        Response<List<ProductResponse>> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        productController.getProductsByCategoryId("categoryId",blimartId,1, 100)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetProductsByCategoryIdCommand.class, request);
    }
    @Test
    public void test_getTopSoldProducts() {
        List<ProductResponse> data = Arrays.asList(productResponse1, productResponse2);
        PagingRequest request = PagingRequest.builder().page(0).size(100).blimartId(blimartId).build();

        Mono<List<ProductResponse>> response = Mono.just(data);
        Mockito.when(commandExecutor.execute(GetTopSoldProductsCommand.class, request))
                .thenReturn(response);

        Response<List<ProductResponse>> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        productController.getTopSoldProducts(blimartId, 100)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetTopSoldProductsCommand.class, request);
    }
    @Test
    public void test_getPromoProducts() {
        List<ProductResponse> data = Arrays.asList(productResponse1, productResponse2);
        PagingRequest request = PagingRequest.builder().page(0).size(100).blimartId(blimartId).build();

        Mono<List<ProductResponse>> response = Mono.just(data);
        Mockito.when(commandExecutor.execute(GetPromoProductsCommand.class, request))
                .thenReturn(response);

        Response<List<ProductResponse>> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        productController.getPromoProducts(blimartId, 100)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetPromoProductsCommand.class, request);
    }
    @Test
    public void test_getRandomProducts() {
        List<ProductResponse> data = Arrays.asList(productResponse1, productResponse2);
        PagingRequest request = PagingRequest.builder().page(0).size(100).blimartId(blimartId).build();

        Mono<List<ProductResponse>> response = Mono.just(data);
        Mockito.when(commandExecutor.execute(GetRandomProductsCommand.class, request))
                .thenReturn(response);

        Response<List<ProductResponse>> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        productController.getRandomProducts(blimartId, 100)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetRandomProductsCommand.class, request);
    }
    @Test
    public void test_getRandomRelatedProducts() {
        List<ProductResponse> data = Arrays.asList(productResponse1, productResponse2);
        PagingRequest request = PagingRequest.builder().page(0).size(100).blimartId(blimartId).build();

        Mono<List<ProductResponse>> response = Mono.just(data);
        Mockito.when(commandExecutor.execute(GetRandomByRelatedProductsCommand.class, request))
                .thenReturn(response);


        Response<List<ProductResponse>> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        productController.getRandomRelatedProducts(blimartId, productId1, 100)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetRandomByRelatedProductsCommand.class, request);
    }
    @Test
    public void test_getByKeyword() {
        String keyword = "keyword";
        List<ProductResponse> data = Arrays.asList(productResponse1, productResponse2);
        SearchRequest request = new SearchRequest();
        request.setKeyword(keyword);
        request.setPage(0);
        request.setSize(100);
        request.setBlimartId(blimartId);
        PagingResponse<ProductResponse> pagingResponse = new PagingResponse(
                data, Paging.builder().itemPerPage(100).page(1).totalItem(2).totalPage(1).build()
        );

        Mono<PagingResponse<ProductResponse>> response = Mono.just(pagingResponse);
        Mockito.when(commandExecutor.execute(GetProductsByKeywordCommand.class, request))
                .thenReturn(response);


        Response<List<ProductResponse>> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        productController.getByKeyword(blimartId, keyword, 1, 100)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetProductsByKeywordCommand.class, request);
    }

    @Test
    public void test_addProduct() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        ProductRequest productRequest = ProductRequest.builder()
                .blimartId(blimartId)
                .categoryId("category")
                .discount(10)
                .mark1("Ae")
                .mark2("Bw")
                .name("product_name")
                .price(10000)
                .stock(10)
                .variant("Varian")
                .build();
        productRequest.setRequester(principal1.getName());


        Mono<ProductResponse> response = Mono.just(productResponse1);
        Mockito.when(commandExecutor.execute(CreateProductCommand.class, productRequest))
                .thenReturn(response);

        Response<ProductResponse> expected = new Response<>();
        expected.setData(productResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        productController.addProduct(productRequest,principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(CreateProductCommand.class, productRequest);
    }
    @Test
    public void test_updateProduct() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        ProductRequest productRequest = ProductRequest.builder()
                .blimartId(blimartId)
                .categoryId("category")
                .discount(10)
                .mark1("Ae")
                .mark2("Bw")
                .name("product_name")
                .price(10000)
                .stock(10)
                .variant("Varian")
                .build();
        productRequest.setRequester(principal1.getName());


        Mono<ProductResponse> response = Mono.just(productResponse1);
        Mockito.when(commandExecutor.execute(UpdateProductCommand.class, productRequest))
                .thenReturn(response);

        Response<ProductResponse> expected = new Response<>();
        expected.setData(productResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        productController.updateProduct(productRequest,principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(UpdateProductCommand.class, productRequest);
    }
    @Test
    public void test_deleteProduct() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        BaseRequest baseRequest = new BaseRequest(productId1, principal1.getName());


        Mono<MessageResponse> response = Mono.just(MessageResponse.SUCCESS);
        Mockito.when(commandExecutor.execute(DeleteProductCommand.class, baseRequest))
                .thenReturn(response);

        Response<MessageResponse> expected = new Response<>();
        expected.setData(MessageResponse.SUCCESS);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        productController.deleteProduct(productId1,principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(DeleteProductCommand.class, baseRequest);
    }

    @Test
    public void test_getProductPhoto() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));

        Mono<byte[]> response = Mono.just(new byte[]{});
        Mockito.when(commandExecutor.execute(GetPhotoProductCommand.class, productId1))
                .thenReturn(response);

        byte[] expected = response.block();

        productController.getPhoto(productId1)
                .subscribe(res -> {
                    Assert.assertEquals(res, expected);
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetPhotoProductCommand.class, productId1);
    }

    @Test
    public void test_uploadProductPhoto() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));

        ImageRequest request = new ImageRequest();
        request.setId(productId1);
        request.setRequester(principal1.getName());
        Mono<MessageResponse> response = Mono.just(MessageResponse.SUCCESS);
        Mockito.when(commandExecutor.execute(UploadPhotoProductCommand.class, request))
                .thenReturn(response);


        Response<MessageResponse> expected = new Response<>();
        expected.setData(MessageResponse.SUCCESS);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());


        productController.uploadPhoto(null, productId1, principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(UploadPhotoProductCommand.class, request);
    }
}
