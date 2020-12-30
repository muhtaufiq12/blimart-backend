package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.bliblifuturebackend.bliblimart.command.*;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.request.DirectionRequest;
import com.bliblifuturebackend.bliblimart.model.request.MarkRequest;
import com.bliblifuturebackend.bliblimart.model.request.RouteRequest;
import com.bliblifuturebackend.bliblimart.model.response.MarkResponse;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.model.response.RouteResponse;
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
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = ARController.class)
public class ARControllerTests {

    @MockBean
    private CommandExecutor commandExecutor;

    @Autowired
    private ARController arController;

    private String userId1;

    private User user1;

    private Principal principal1;

    private MarkResponse mark1, mark2;

    @Before
    public void setup() {
        userId1 = "userId1";

        user1 = User.builder().username("user1").password("pass1").build();
        user1.setId(userId1);

        principal1 = new Principal() {
            @Override
            public String getName() {
                return user1.getUsername();
            }
        };

        mark1 = MarkResponse.builder()
                .name("mark1")
                .x(0)
                .y(1)
                .path("path1")
                .build();
        mark1.setId("id1");

        mark2 = MarkResponse.builder()
                .name("mark2")
                .x(1)
                .y(1)
                .path("path2")
                .build();
        mark2.setId("id2");
    }

    @Test
    public void test_getAllMarks(){
        List<MarkResponse> data = Arrays.asList(mark1, mark2);

        Mono<List<MarkResponse>> response = Mono.just(data);

        Mockito.when(commandExecutor.execute(GetAllMarksCommand.class, ""))
                .thenReturn(response);

        Response<List<MarkResponse>> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        arController.getAllMarks()
                .subscribe(responses -> {
                    Assert.assertEquals(responses.getCode(), expected.getCode());
                    Assert.assertEquals(responses.getStatus(), expected.getStatus());
                    for (int i = 0; i < expected.getData().size(); i++) {
                        MarkResponse res = responses.getData().get(i);
                        MarkResponse ex = expected.getData().get(i);
                        Assert.assertEquals(ex.getName(), res.getName());
                        Assert.assertEquals(ex.getPath(), res.getPath());
                        Assert.assertEquals(ex.getX(), res.getX());
                        Assert.assertEquals(ex.getY(), res.getY());
                    }
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetAllMarksCommand.class, "");
    }

    @Test
    public void test_getRoute(){
        String productId = "productId1";

        RouteResponse data = RouteResponse.builder()
                .rotation(90)
                .next("mark2")
                .build();
        data.setId("routeId1");

        Mono<RouteResponse> response = Mono.just(data);

        RouteRequest request = RouteRequest.builder()
                .productId(productId)
                .startingMark("mark1")
                .build();

        Mockito.when(commandExecutor.execute(GetRouteCommand.class, request))
                .thenReturn(response);

        Response<RouteResponse> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        arController.getRoute(request)
                .subscribe(responses -> {
                    Assert.assertEquals(responses.getCode(), expected.getCode());
                    Assert.assertEquals(responses.getStatus(), expected.getStatus());

                    RouteResponse res = responses.getData();
                    RouteResponse ex = expected.getData();
                    Assert.assertEquals(ex.getNext(), res.getNext());
                    Assert.assertEquals(ex.getRotation(), res.getRotation());
                    Assert.assertEquals(ex.getId(), res.getId());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetRouteCommand.class, request);
    }

    @Test
    public void test_getDirection(){
        String routeId = "routeId1";
        String mark = mark1.getName();

        RouteResponse data = RouteResponse.builder()
                .rotation(90)
                .next("mark2")
                .build();

        Mono<RouteResponse> response = Mono.just(data);

        DirectionRequest request = DirectionRequest.builder().mark(mark).build();

        Mockito.when(commandExecutor.execute(GetDirectionCommand.class, request))
                .thenReturn(response);

        Response<RouteResponse> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());
        arController.getDirection(routeId, mark)
                .subscribe(responses -> {
                    Assert.assertEquals(responses.getCode(), expected.getCode());
                    Assert.assertEquals(responses.getStatus(), expected.getStatus());

                    RouteResponse res = responses.getData();
                    RouteResponse ex = expected.getData();
                    Assert.assertEquals(ex.getNext(), res.getNext());
                    Assert.assertEquals(ex.getRotation(), res.getRotation());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetDirectionCommand.class, request);
    }

    @Test
    public void test_createNewMark(){
        Mono<MarkResponse> response = Mono.just(mark1);

        MarkRequest request = MarkRequest.builder()
                .name(mark1.getName())
                .path(mark1.getPath())
                .x(mark1.getX())
                .y(mark1.getY())
                .build();
        request.setRequester(principal1.getName());

        Mockito.when(commandExecutor.execute(CreateNewMarkCommand.class, request))
                .thenReturn(response);

        Response<MarkResponse> expected = new Response<>();
        expected.setData(mark1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        arController.createNewMark(request, principal1)
                .subscribe(responses -> {
                    Assert.assertEquals(responses.getCode(), expected.getCode());
                    Assert.assertEquals(responses.getStatus(), expected.getStatus());

                    MarkResponse res = responses.getData();
                    MarkResponse ex = expected.getData();
                    Assert.assertEquals(ex.getId(), res.getId());
                    Assert.assertEquals(ex.getName(), res.getName());
                    Assert.assertEquals(ex.getPath(), res.getPath());
                    Assert.assertEquals(ex.getX(), res.getX());
                    Assert.assertEquals(ex.getY(), res.getY());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(CreateNewMarkCommand.class, request);
    }

    @Test
    public void test_updateMark(){
        Mono<MarkResponse> response = Mono.just(mark1);

        MarkRequest request = MarkRequest.builder()
                .name(mark1.getName())
                .path(mark1.getPath())
                .x(mark1.getX())
                .y(mark1.getY())
                .build();
        request.setId(mark1.getId());
        request.setRequester(principal1.getName());

        Mockito.when(commandExecutor.execute(UpdateMarkCommand.class, request))
                .thenReturn(response);

        Response<MarkResponse> expected = new Response<>();
        expected.setData(mark1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        arController.updateMark(request, principal1)
                .subscribe(responses -> {
                    Assert.assertEquals(responses.getCode(), expected.getCode());
                    Assert.assertEquals(responses.getStatus(), expected.getStatus());

                    MarkResponse res = responses.getData();
                    MarkResponse ex = expected.getData();
                    Assert.assertEquals(ex.getId(), res.getId());
                    Assert.assertEquals(ex.getName(), res.getName());
                    Assert.assertEquals(ex.getPath(), res.getPath());
                    Assert.assertEquals(ex.getX(), res.getX());
                    Assert.assertEquals(ex.getY(), res.getY());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(UpdateMarkCommand.class, request);
    }

    @Test
    public void test_deleteMark(){
        Mono<MessageResponse> response = Mono.just(MessageResponse.SUCCESS);

        Mockito.when(commandExecutor.execute(DeleteMarkCommand.class, mark1.getId()))
                .thenReturn(response);

        Response<MessageResponse> expected = new Response<>();
        expected.setData(MessageResponse.SUCCESS);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());
      
        arController.deleteMark(mark1.getId())
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(DeleteMarkCommand.class, mark1.getId());
    }
}
