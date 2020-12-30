package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.paging.Paging;
import com.blibli.oss.common.response.Response;
import com.bliblifuturebackend.bliblimart.command.*;
import com.bliblifuturebackend.bliblimart.model.entity.Cart;
import com.bliblifuturebackend.bliblimart.model.entity.Supply;
import com.bliblifuturebackend.bliblimart.model.request.CartWishlistRequest;
import com.bliblifuturebackend.bliblimart.model.request.ListIdRequest;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
import com.bliblifuturebackend.bliblimart.model.request.SupplyRequest;
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
@WebFluxTest(controllers = SupplyController.class)
public class SupplyControllerTests {

    @MockBean
    private CommandExecutor commandExecutor;

    @Autowired
    private SupplyController supplyController;



    private String supplyId1, supplyId2;
    private String productId = "productId";
    private String productId2 = "productId2";
    private  String userId = "userId";
    private  String supplierId = "supplierId";

    private Supply supply, supply2;

    private SupplyResponse supplyResponse, supplyResponse1;

    private Principal principal1;

    @Before
    public void setup() throws ParseException {

        supplyId1 = "cartId1";
        supplyId2 = "cartId2";

        supply = Supply.builder()
                .productId(productId)
                .qty(1)
                .supplierId(supplierId)
                .build();
        supply.setId(supplyId1);

        supply2 = Supply.builder()
                .productId(productId2)
                .qty(1)
                .supplierId(supplierId)
                .build();
        supply2.setId(supplyId2);


        supplyResponse = supply.createResponse();

        supplyResponse = supply2.createResponse();

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
        List<SupplyResponse> data = Arrays.asList(supplyResponse, supplyResponse1);
        PagingResponse<SupplyResponse> pagingResponse = new PagingResponse<>(
                data, new Paging(1, 1, 100)
        );
        PagingRequest request = PagingRequest.builder().page(0).size(100).build();

        Mono<PagingResponse<SupplyResponse>> response = Mono.just(pagingResponse);

        Mockito.when(commandExecutor.execute(GetAllSuppliesCommand.class, request))
                .thenReturn(response);

        Response<List<SupplyResponse>> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        supplyController.getAllSupplies(1, 100)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetAllSuppliesCommand.class, request);
    }

    @Test
    public void test_add() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        SupplyRequest supplyRequest = SupplyRequest.builder()
                .productId(productId)
                .qty(1)
                .supplierId(supplierId)
                .build();
        supplyRequest.setRequester(principal1.getName());


        Mono<SupplyResponse> response = Mono.just(supplyResponse);
        Mockito.when(commandExecutor.execute(CreateSupplyCommand.class, supplyRequest))
                .thenReturn(response);

        Response<SupplyResponse> expected = new Response<>();
        expected.setData(supplyResponse);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        supplyController.addSupply(supplyRequest,principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(CreateSupplyCommand.class, supplyRequest);
    }
//    @Test
//    public void test_delete() {
////        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
//
//        ListIdRequest cartWishlistRequest = ListIdRequest.builder()
//                .ids(Arrays.asList(new String[]{productId, productId2}))
//                .requester(principal1.getName())
//                .build();
//
//
//
//        Mono<MessageResponse> response = Mono.just(MessageResponse.SUCCESS);
//        Mockito.when(commandExecutor.execute(DeleteCartsCommand.class, cartWishlistRequest))
//                .thenReturn(response);
//
//        Response<MessageResponse> expected = new Response<>();
//        expected.setData(MessageResponse.SUCCESS);
//        expected.setCode(HttpStatus.OK.value());
//        expected.setStatus(HttpStatus.OK.name());
//
//        supplyController.deleteCarts(cartWishlistRequest,principal1)
//                .subscribe(res -> {
//                    Assert.assertEquals(res.getCode(), expected.getCode());
//                    Assert.assertEquals(res.getStatus(), expected.getStatus());
//                    Assert.assertEquals(res.getData(), expected.getData());
//                });
//
//        Mockito.verify(commandExecutor, Mockito.times(1)).execute(DeleteCartsCommand.class, cartWishlistRequest);
//    }
}
