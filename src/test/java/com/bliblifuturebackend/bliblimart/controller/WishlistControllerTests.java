package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.paging.Paging;
import com.blibli.oss.common.response.Response;
import com.bliblifuturebackend.bliblimart.command.*;
import com.bliblifuturebackend.bliblimart.model.entity.Cart;
import com.bliblifuturebackend.bliblimart.model.entity.Wishlist;
import com.bliblifuturebackend.bliblimart.model.request.CartWishlistRequest;
import com.bliblifuturebackend.bliblimart.model.request.ListIdRequest;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
import com.bliblifuturebackend.bliblimart.model.response.CartWishlistResponse;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.model.response.PagingResponse;
import com.bliblifuturebackend.bliblimart.model.response.SupplyResponse;
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
@WebFluxTest(controllers = WishlistController.class)
public class WishlistControllerTests {

    @MockBean
    private CommandExecutor commandExecutor;

    @Autowired
    private WishlistController wishlistController;



    private String wishlistId1, wishlistId2;
    private String productId = "productId";
    private String productId2 = "productId2";
    private  String userId = "userId";

    private Wishlist wishlist1, wishlist2;

    private CartWishlistResponse cartWishlistResponse1, cartWishlistResponse2;

    private Principal principal1;

    @Before
    public void setup() throws ParseException {

        wishlistId1 = "wishlistId1";
        wishlistId2 = "wishlistId2";

        wishlist1 = Wishlist.builder()
                .productId(productId)
                .userId(userId)
                .build();
        wishlist1.setId(wishlistId1);

        wishlist2 = Wishlist.builder()
                .productId(productId)
                .userId(userId)
                .build();
        wishlist2.setId(wishlistId2);


        cartWishlistResponse1 = wishlist1.createResponse();

        cartWishlistResponse1 = wishlist2.createResponse();

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
        request.setRequester(principal1.getName());
        PagingResponse<CartWishlistResponse> pagingResponse = new PagingResponse<>(
                data, new Paging(1, 1, 100)
        );
        Mono<PagingResponse<CartWishlistResponse>> response = Mono.just(pagingResponse);

        Mockito.when(commandExecutor.execute(GetUserWishlistCommand.class, request))
                .thenReturn(response);

        Response<List<CartWishlistResponse>> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        wishlistController.getMyWishlist(1, 100, principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetUserWishlistCommand.class, request);
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
        Mockito.when(commandExecutor.execute(CreateWishlistCommand.class, cartWishlistRequest))
                .thenReturn(response);

        Response<MessageResponse> expected = new Response<>();
        expected.setData(MessageResponse.SUCCESS);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        wishlistController.addWishlist(cartWishlistRequest,principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(CreateWishlistCommand.class, cartWishlistRequest);
    }
    @Test
    public void test_delete() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        CartWishlistRequest cartWishlistRequest = CartWishlistRequest.builder()
                .productId("0")
                .build();
        cartWishlistRequest.setId(wishlistId1);
        cartWishlistRequest.setRequester(principal1.getName());



        Mono<MessageResponse> response = Mono.just(MessageResponse.SUCCESS);
        Mockito.when(commandExecutor.execute(DeleteWishlistCommand.class, cartWishlistRequest))
                .thenReturn(response);

        Response<MessageResponse> expected = new Response<>();
        expected.setData(MessageResponse.SUCCESS);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        wishlistController.deleteWishlist(wishlistId1,principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(DeleteWishlistCommand.class, cartWishlistRequest);
    }
}
