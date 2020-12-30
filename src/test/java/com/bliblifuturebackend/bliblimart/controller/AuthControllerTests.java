package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.bliblifuturebackend.bliblimart.command.LoginCommand;
import com.bliblifuturebackend.bliblimart.command.LogoutCommand;
import com.bliblifuturebackend.bliblimart.command.RefreshAuthCommand;
import com.bliblifuturebackend.bliblimart.command.RegisterCommand;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.request.UserRequest;
import com.bliblifuturebackend.bliblimart.model.response.JwtResponse;
import com.bliblifuturebackend.bliblimart.model.response.UserResponse;
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

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = AuthController.class)
public class AuthControllerTests {

    @MockBean
    private CommandExecutor commandExecutor;

    @Autowired
    private AuthController authController;

    private String userId1, userId2;

    private User user1, user2;

    private UserResponse userResponse1, userResponse2;

    private Principal principal1;

    private String token1, token2;

    @Before
    public void setup() {
        userId1 = "userId1";
        userId2 = "userId2";

        user1 = User.builder().username("user1").password("pass1").email("user1@gmail.com")
                .isActive(true).isBlocked(false).isAdmin(false).photoUrl("/user-photo/" + userId1).build();
        user1.setId(userId1);

        user2 = User.builder().username("user2").password("pass2").email("user2@gmail.com")
                .isActive(true).isBlocked(false).isAdmin(false).photoUrl("/user-photo/" + userId2).build();
        user2.setId(userId2);

        userResponse1 = user1.createResponse();

        userResponse2 = user2.createResponse();

        principal1 = new Principal() {
            @Override
            public String getName() {
                return user1.getUsername();
            }
        };

        token1 = "token1";
        token2 = "token2";
    }

    @Test
    public void test_register(){
        Mono<UserResponse> response = Mono.just(userResponse1);

        UserRequest request = UserRequest.builder()
                .username(user1.getUsername())
                .password(user1.getPassword())
                .email(user1.getEmail())
                .isAdmin(user1.getIsAdmin())
                .build();

        Mockito.when(commandExecutor.execute(RegisterCommand.class, request))
                .thenReturn(response);

        Response<UserResponse> expected = new Response<>();
        expected.setData(userResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        authController.register(request)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData().getId(), expected.getData().getId());
                    Assert.assertEquals(res.getData().getUsername(), expected.getData().getUsername());
                    Assert.assertEquals(res.getData().getPhotoUrl(), expected.getData().getPhotoUrl());
                    Assert.assertEquals(res.getData().getIsBlocked(), expected.getData().getIsBlocked());
                    Assert.assertEquals(res.getData().getIsAdmin(), expected.getData().getIsAdmin());
                    Assert.assertEquals(res.getData().getIsActive(), expected.getData().getIsActive());
                    Assert.assertEquals(res.getData().getEmail(), expected.getData().getEmail());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(RegisterCommand.class, request);
    }

    @Test
    public void test_login(){
        JwtResponse jwtResponse = JwtResponse.builder()
                .jwtToken(token1)
                .userResponse(userResponse1)
                .build();

        Mono<JwtResponse> response = Mono.just(jwtResponse);

        UserRequest request = UserRequest.builder()
                .username(user1.getUsername())
                .password(user1.getPassword())
                .email(user1.getEmail())
                .isAdmin(user1.getIsAdmin())
                .build();

        Mockito.when(commandExecutor.execute(LoginCommand.class, request))
                .thenReturn(response);

        Response<JwtResponse> expected = new Response<>();
        expected.setData(jwtResponse);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        authController.login(request)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData().getJwtToken(), expected.getData().getJwtToken());

                    UserResponse data = res.getData().getUserResponse();
                    UserResponse ex = expected.getData().getUserResponse();

                    Assert.assertEquals(data.getId(), ex.getId());
                    Assert.assertEquals(data.getUsername(), ex.getUsername());
                    Assert.assertEquals(data.getPhotoUrl(), ex.getPhotoUrl());
                    Assert.assertEquals(data.getIsBlocked(), ex.getIsBlocked());
                    Assert.assertEquals(data.getIsAdmin(), ex.getIsAdmin());
                    Assert.assertEquals(data.getIsActive(), ex.getIsActive());
                    Assert.assertEquals(data.getEmail(), ex.getEmail());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(LoginCommand.class, request);
    }

    @Test
    public void test_refresh(){
        JwtResponse jwtResponse = JwtResponse.builder()
                .jwtToken(token1)
                .userResponse(userResponse1)
                .build();

        Mono<JwtResponse> response = Mono.just(jwtResponse);

        Mockito.when(commandExecutor.execute(RefreshAuthCommand.class, principal1))
                .thenReturn(response);

        Response<JwtResponse> expected = new Response<>();
        expected.setData(jwtResponse);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        authController.refresh(principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData().getJwtToken(), expected.getData().getJwtToken());

                    UserResponse data = res.getData().getUserResponse();
                    UserResponse ex = expected.getData().getUserResponse();

                    Assert.assertEquals(data.getId(), ex.getId());
                    Assert.assertEquals(data.getUsername(), ex.getUsername());
                    Assert.assertEquals(data.getPhotoUrl(), ex.getPhotoUrl());
                    Assert.assertEquals(data.getIsBlocked(), ex.getIsBlocked());
                    Assert.assertEquals(data.getIsAdmin(), ex.getIsAdmin());
                    Assert.assertEquals(data.getIsActive(), ex.getIsActive());
                    Assert.assertEquals(data.getEmail(), ex.getEmail());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(RefreshAuthCommand.class, principal1);
    }

    @Test
    public void test_logout(){
        JwtResponse jwtResponse = JwtResponse.builder()
                .jwtToken(token1)
                .userResponse(userResponse1)
                .build();

        Mono<JwtResponse> response = Mono.just(jwtResponse);

        Mockito.when(commandExecutor.execute(LogoutCommand.class, principal1))
                .thenReturn(response);

        Response<JwtResponse> expected = new Response<>();
        expected.setData(jwtResponse);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        authController.logout(principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData().getJwtToken(), expected.getData().getJwtToken());

                    UserResponse data = res.getData().getUserResponse();
                    UserResponse ex = expected.getData().getUserResponse();

                    Assert.assertEquals(data.getId(), ex.getId());
                    Assert.assertEquals(data.getUsername(), ex.getUsername());
                    Assert.assertEquals(data.getPhotoUrl(), ex.getPhotoUrl());
                    Assert.assertEquals(data.getIsBlocked(), ex.getIsBlocked());
                    Assert.assertEquals(data.getIsAdmin(), ex.getIsAdmin());
                    Assert.assertEquals(data.getIsActive(), ex.getIsActive());
                    Assert.assertEquals(data.getEmail(), ex.getEmail());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(LogoutCommand.class, principal1);
    }

}
