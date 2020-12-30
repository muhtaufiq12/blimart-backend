package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.bliblifuturebackend.bliblimart.command.*;
import com.bliblifuturebackend.bliblimart.model.entity.Cart;
import com.bliblifuturebackend.bliblimart.model.request.CartWishlistRequest;
import com.bliblifuturebackend.bliblimart.model.request.CategoryRequest;
import com.bliblifuturebackend.bliblimart.model.request.ListIdRequest;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
import com.bliblifuturebackend.bliblimart.model.response.CartWishlistResponse;
import com.bliblifuturebackend.bliblimart.model.response.CategoryResponse;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
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
@WebFluxTest(controllers = CartController.class)
public class CartControllerTests {

    @MockBean
    private CommandExecutor commandExecutor;

    @Autowired
    private CartController cartController;



    private String cartId1, cartId2;
    private String productId = "productId";
    private String productId2 = "productId2";
    private  String userId = "userId";

    private Cart cart1, cart2;

    private CartWishlistResponse cartWishlistResponse1, cartWishlistResponse2;

    private Principal principal1;

    @Before
    public void setup() throws ParseException {

        cartId1 = "cartId1";
        cartId2 = "cartId2";

        cart1 = Cart.builder()
                .productId(productId)
                .userId(userId)
                .totalItem(1)
                .build();
        cart1.setId(cartId1);

        cart2 = Cart.builder()
                .productId(productId)
                .userId(userId)
                .totalItem(1)
                .build();
        cart2.setId(cartId2);


        cartWishlistResponse1 = cart1.createResponse();

        cartWishlistResponse1 = cart2.createResponse();

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
        List<CartWishlistResponse> data = Arrays.asList(cartWishlistResponse1, cartWishlistResponse2);
        PagingRequest request = PagingRequest.builder().page(0).size(100).build();

        Mono<List<CartWishlistResponse>> response = Mono.just(data);

        Mockito.when(commandExecutor.execute(GetUserCartCommand.class, principal1.getName()))
                .thenReturn(response);

        Response<List<CartWishlistResponse>> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        cartController.getMyCart(principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetUserCartCommand.class, principal1.getName());
    }

    @Test
    public void test_add() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        CartWishlistRequest cartWishlistRequest = CartWishlistRequest.builder()
                .productId(productId)
                .totalItem(1)
                .build();
        cartWishlistRequest.setRequester(principal1.getName());


        Mono<MessageResponse> response = Mono.just(MessageResponse.SUCCESS);
        Mockito.when(commandExecutor.execute(CreateCartCommand.class, cartWishlistRequest))
                .thenReturn(response);

        Response<MessageResponse> expected = new Response<>();
        expected.setData(MessageResponse.SUCCESS);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        cartController.addCart(cartWishlistRequest,principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(CreateCartCommand.class, cartWishlistRequest);
    }
    @Test
    public void test_update() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        CartWishlistRequest cartWishlistRequest = CartWishlistRequest.builder()
                .productId(productId)
                .totalItem(1)
                .build();
        cartWishlistRequest.setRequester(principal1.getName());



        Mono<MessageResponse> response = Mono.just(MessageResponse.SUCCESS);
        Mockito.when(commandExecutor.execute(UpdateCartQtyCommand.class, cartWishlistRequest))
                .thenReturn(response);

        Response<MessageResponse> expected = new Response<>();
        expected.setData(MessageResponse.SUCCESS);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        cartController.updateQtyCart(cartWishlistRequest,principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(UpdateCartQtyCommand.class, cartWishlistRequest);
    }
    @Test
    public void test_delete() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));

        ListIdRequest cartWishlistRequest = ListIdRequest.builder()
                .ids(Arrays.asList(new String[]{productId, productId2}))
                .requester(principal1.getName())
                .build();



        Mono<MessageResponse> response = Mono.just(MessageResponse.SUCCESS);
        Mockito.when(commandExecutor.execute(DeleteCartsCommand.class, cartWishlistRequest))
                .thenReturn(response);

        Response<MessageResponse> expected = new Response<>();
        expected.setData(MessageResponse.SUCCESS);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        cartController.deleteCarts(cartWishlistRequest,principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(DeleteCartsCommand.class, cartWishlistRequest);
    }
}
