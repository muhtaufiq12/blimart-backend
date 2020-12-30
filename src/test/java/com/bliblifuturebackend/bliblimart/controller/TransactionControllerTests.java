package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.paging.Paging;
import com.blibli.oss.common.response.Response;
import com.bliblifuturebackend.bliblimart.command.*;
import com.bliblifuturebackend.bliblimart.model.entity.Transaction;
import com.bliblifuturebackend.bliblimart.model.request.*;
import com.bliblifuturebackend.bliblimart.model.response.*;
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
@WebFluxTest(controllers = TransactionController.class)
public class TransactionControllerTests {

    @MockBean
    private CommandExecutor commandExecutor;

    @Autowired
    private TransactionController transactionController;



    private String transactionId1, transactionId2;
    private String productId = "productId";
    private String productId2 = "productId2";
    private  String userId = "userId";

    private Transaction transaction1, transaction2;

    private TransactionResponse transactionResponse1, transactionResponse2;

    private Principal principal1;

    @Before
    public void setup() throws ParseException {

        transactionId1 = "transactionId1";
        transactionId2 = "transactionId2";

        transaction1 = Transaction.builder()
                .status("OK")
                .total(200)
                .transactionNumber("029302932")
                .userId(userId)
                .build();
        transaction1.setId(transactionId1);

        transaction2 = Transaction.builder()
                .status("OK")
                .total(200)
                .transactionNumber("029302322932")
                .userId(userId)
                .build();
        transaction2.setId(transactionId2);


        transactionResponse1 = transaction1.createResponse();

        transactionResponse1 = transaction2.createResponse();

        principal1 = new Principal() {
            @Override
            public String getName() {
                return "username";
            }
        };

    }

    @Test
    public void test_getById() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        BaseRequest request = new BaseRequest(transactionId1, principal1.getName());
        Mono<TransactionResponse> response = Mono.just(transactionResponse1);

        Mockito.when(commandExecutor.execute(GetTransactionCommand.class, request))
                .thenReturn(response);

        Response<TransactionResponse> expected = new Response<>();
        expected.setData(transactionResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        transactionController.getTransactionById(transactionId1, principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetTransactionCommand.class, request);
    }
    @Test
    public void test_getAll() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        List<TransactionResponse> data = Arrays.asList(transactionResponse1, transactionResponse2);
        PagingRequest request = PagingRequest.builder().page(0).size(100).build();

        Mono<List<TransactionResponse>> response = Mono.just(data);

        Mockito.when(commandExecutor.execute(GetUserActiveTransactionCommand.class, principal1.getName()))
                .thenReturn(response);

        Response<List<TransactionResponse>> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        transactionController.getActiveTransaction(principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetUserActiveTransactionCommand.class, principal1.getName());
    }

    @Test
    public void test_getHistory() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        List<TransactionDetailResponse> data = transactionResponse1.getDetailResponses();
        PagingRequest request = PagingRequest.builder().page(0).size(100).build();
        request.setRequester(principal1.getName());
        PagingResponse<TransactionDetailResponse> pagingResponse = new PagingResponse(
                data, Paging.builder().itemPerPage(100).page(1).totalItem(2).totalPage(1).build()
        );


        Mono<PagingResponse<TransactionDetailResponse>> response = Mono.just(pagingResponse);

        Mockito.when(commandExecutor.execute(GetUserHistoryTransactionCommand.class, request))
                .thenReturn(response);

        Response<List<TransactionDetailResponse>> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        transactionController.getHistoryTransaction(1, 100, principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetUserHistoryTransactionCommand.class, request);
    }

    @Test
    public void test_add() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        ProductTransactionRequest productTransactionRequest1 = ProductTransactionRequest.builder()
                .productId(productId)
                .totalItem(1).build();
        ProductTransactionRequest productTransactionRequest2 = ProductTransactionRequest.builder()
                .productId(productId2)
                .totalItem(1).build();
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .products(Arrays.asList(productTransactionRequest1, productTransactionRequest2))
                .build();
        transactionRequest.setRequester(principal1.getName());


        Mono<TransactionResponse> response = Mono.just(transactionResponse1);
        Mockito.when(commandExecutor.execute(CreateTransactionCommand.class, transactionRequest))
                .thenReturn(response);

        Response<TransactionResponse> expected = new Response<>();
        expected.setData(transactionResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        transactionController.addTransaction(transactionRequest,principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(CreateTransactionCommand.class, transactionRequest);
    }
    @Test
    public void test_cancel() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        BaseRequest request = new BaseRequest(transactionId1, principal1.getName());

        Mono<TransactionResponse> response = Mono.just(transactionResponse1);
        Mockito.when(commandExecutor.execute(CancelTransactionCommand.class, request))
                .thenReturn(response);

        Response<TransactionResponse> expected = new Response<>();
        expected.setData(transactionResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        transactionController.cancelTransaction(transactionId1,principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(CancelTransactionCommand.class, request);
    }
    @Test
    public void test_payment() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));

        PaymentRequest request = PaymentRequest.builder().username(principal1.getName()).transactionId(transactionId1).build();


        Mono<TransactionResponse> response = Mono.just(transactionResponse1);
        Mockito.when(commandExecutor.execute(PaymentCommand.class, request))
                .thenReturn(response);

        Response<TransactionResponse> expected = new Response<>();
        expected.setData(transactionResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        transactionController.payment(transactionId1,principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(PaymentCommand.class, request);
    }
}
