package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.paging.Paging;
import com.blibli.oss.common.response.Response;
import com.bliblifuturebackend.bliblimart.command.*;
import com.bliblifuturebackend.bliblimart.model.entity.Supplier;
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
@WebFluxTest(controllers = SupplierController.class)
public class SupplierControllerTests {

    @MockBean
    private CommandExecutor commandExecutor;

    @Autowired
    private SupplierController supplierController;



    private String supplierId1, supplierId2;
    private String productId = "productId";
    private String productId2 = "productId2";
    private  String userId = "userId";

    private Supplier supplier1, supplier2;

    private SupplierResponse supplierResponse1, supplierResponse2;

    private Principal principal1;

    @Before
    public void setup() throws ParseException {

        supplierId1 = "supplierId1";
        supplierId2 = "supplierId2";

        supplier1 = Supplier.builder()
                .name("supplier_name")
                .build();
        supplier1.setId(supplierId1);

        supplier2 = Supplier.builder()
                .name("supplier_name")
                .build();
        supplier2.setId(supplierId2);


        supplierResponse1 = supplier1.createResponse();

        supplierResponse1 = supplier2.createResponse();

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
        Mono<SupplierResponse> res = Mono.just(supplierResponse1);

        Mockito.when(commandExecutor.execute(GetSupplierCommand.class, supplierId1))
                .thenReturn(res);

        Response<SupplierResponse> expected = new Response<>();
        expected.setData(supplierResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        supplierController.getSupplierById(supplierId1)
                .subscribe(response -> {
                    Assert.assertEquals(response.getCode(), expected.getCode());
                    Assert.assertEquals(response.getStatus(), expected.getStatus());
                    Assert.assertEquals(response.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetSupplierCommand.class, supplierId1);
    }
    @Test
    public void test_getAll() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        List<SupplierResponse> data = Arrays.asList(supplierResponse1, supplierResponse2);
        PagingRequest request = PagingRequest.builder().page(0).size(100).build();

        PagingResponse<SupplierResponse> pagingResponse = new PagingResponse<>(
                data, new Paging(1, 1, 100)
        );
        Mono<PagingResponse<SupplierResponse>> response = Mono.just(pagingResponse);

        Mockito.when(commandExecutor.execute(GetAllSuppliersCommand.class, request))
                .thenReturn(response);

        Response<List<SupplierResponse>> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        supplierController.getAllSuppliers(1, 100)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetAllSuppliersCommand.class, request);
    }

    @Test
    public void test_add() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        SupplierRequest supplierRequest = SupplierRequest.builder()
                .email("email@gmai.com")
                .name("name")
                .phone("090920323")
                .build();
        supplierRequest.setRequester(principal1.getName());


        Mono<SupplierResponse> response = Mono.just(supplierResponse1);
        Mockito.when(commandExecutor.execute(CreateSupplierCommand.class, supplierRequest))
                .thenReturn(response);

        Response<SupplierResponse> expected = new Response<>();
        expected.setData(supplierResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        supplierController.addSupplier(supplierRequest,principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(CreateSupplierCommand.class, supplierRequest);
    }
    @Test
    public void test_update() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        SupplierRequest supplierRequest = SupplierRequest.builder()
                .email("email@gmai.com")
                .name("name")
                .phone("090920323")
                .build();
        supplierRequest.setRequester(principal1.getName());



        Mono<SupplierResponse> response = Mono.just(supplierResponse1);
        Mockito.when(commandExecutor.execute(UpdateSupplierCommand.class, supplierRequest))
                .thenReturn(response);

        Response<SupplierResponse> expected = new Response<>();
        expected.setData(supplierResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        supplierController.updateSupplier(supplierRequest,principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(UpdateSupplierCommand.class, supplierRequest);
    }
    @Test
    public void test_delete() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));


        BaseRequest request = new BaseRequest(supplierId1, principal1.getName());
        Mono<MessageResponse> response = Mono.just(MessageResponse.SUCCESS);

        Mockito.when(commandExecutor.execute(DeleteSupplierCommand.class, request))
                .thenReturn(response);

        Response<MessageResponse> expected = new Response<>();
        expected.setData(MessageResponse.SUCCESS);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        supplierController.deleteSupplier(supplierId1,principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(DeleteSupplierCommand.class, request);
    }
}
