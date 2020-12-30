package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.bliblifuturebackend.bliblimart.command.*;
import com.bliblifuturebackend.bliblimart.model.entity.Blimart;
import com.bliblifuturebackend.bliblimart.model.entity.Cart;
import com.bliblifuturebackend.bliblimart.model.request.*;
import com.bliblifuturebackend.bliblimart.model.response.BlimartResponse;
import com.bliblifuturebackend.bliblimart.model.response.CartWishlistResponse;
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
@WebFluxTest(controllers = BlimartController.class)
public class BlimartControllerTests {

    @MockBean
    private CommandExecutor commandExecutor;

    @Autowired
    private BlimartController blimartController;



    private String blimartId1, blimartId2;


    private Blimart blimart1, blimart2;

    private BlimartResponse blimartResponse1, blimartResponse2;

    private Principal principal1;

    @Before
    public void setup() throws ParseException {

        blimartId1 = "cartId1";
        blimartId2 = "cartId2";

        blimart1 = Blimart.builder()
                .name("Blimart1")
                .build();
        blimart1.setId(blimartId1);

        blimart2 = Blimart.builder()
                .name("Blimart2")
                .build();
        blimart2.setId(blimartId2);


        blimartResponse1 = blimart1.createResponse();

        blimartResponse1 = blimart2.createResponse();

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
        List<BlimartResponse> data = Arrays.asList(blimartResponse1, blimartResponse2);

        Mono<List<BlimartResponse>> response = Mono.just(data);

        Mockito.when(commandExecutor.execute(GetAllBlimartsCommand.class, ""))
                .thenReturn(response);

        Response<List<BlimartResponse>> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        blimartController.getAllBlimartss()
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetAllBlimartsCommand.class, "");
    }

    @Test
    public void test_add() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        BlimartRequest blimartRequest = BlimartRequest.builder()
                .name(blimart1.getName())
                .build();
        blimartRequest.setRequester(principal1.getName());


        Mono<BlimartResponse> response = Mono.just(blimartResponse1);
        Mockito.when(commandExecutor.execute(CreateNewBlimartCommand.class, blimartRequest))
                .thenReturn(response);

        Response<BlimartResponse> expected = new Response<>();
        expected.setData(blimartResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        blimartController.createNewBlimart(blimartRequest,principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(CreateNewBlimartCommand.class, blimartRequest);
    }
    @Test
    public void test_update() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        BlimartRequest blimartRequest = BlimartRequest.builder()
                .name(blimart1.getName())
                .build();
        blimartRequest.setRequester(principal1.getName());



        Mono<BlimartResponse> response = Mono.just(blimartResponse1);
        Mockito.when(commandExecutor.execute(UpdateBlimartCommand.class, blimartRequest))
                .thenReturn(response);

        Response<BlimartResponse> expected = new Response<>();
        expected.setData(blimartResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        blimartController.updateBlimart(blimartRequest,principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(UpdateBlimartCommand.class, blimartRequest);
    }
    @Test
    public void test_delete() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));



        Mono<MessageResponse> response = Mono.just(MessageResponse.SUCCESS);
        Mockito.when(commandExecutor.execute(DeleteBlimartCommand.class, blimartId1))
                .thenReturn(response);

        Response<MessageResponse> expected = new Response<>();
        expected.setData(MessageResponse.SUCCESS);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        blimartController.deleteBlimart(blimartId1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(DeleteBlimartCommand.class, blimartId1);
    }
}
