package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.paging.Paging;
import com.blibli.oss.common.response.Response;
import com.bliblifuturebackend.bliblimart.command.*;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.request.ImageRequest;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
import com.bliblifuturebackend.bliblimart.model.request.ProfileRequest;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.model.response.PagingResponse;
import com.bliblifuturebackend.bliblimart.model.response.ProfileResponse;
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

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = UserController.class)
public class UserControllerTests {

    @MockBean
    private CommandExecutor commandExecutor;

    @Autowired
    private UserController userController;

    String dateString1, dateString2;

    private ProfileResponse profileResponse1, profileResponse2;

    private String userId1, userId2;

    private User user1, user2;

    private UserResponse userResponse1, userResponse2;

    private Principal principal1;

    @Before
    public void setup() throws ParseException, IOException {

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

        dateString1 = "2000-01-30";

        dateString2 = "2001-01-30";

        Date date1  = new SimpleDateFormat("yyyy-MM-dd").parse(dateString1);

        Date date2  = new SimpleDateFormat("yyyy-MM-dd").parse(dateString2);

        profileResponse1 = ProfileResponse.builder()
                .name("Profile1")
                .gender(true)
                .birthDate(date1)
                .phone("0123")
                .userId(userId1)
                .build();

        profileResponse2 = ProfileResponse.builder()
                .name("Profile2")
                .gender(false)
                .birthDate(date2)
                .phone("0124")
                .userId(userId2)
                .build();
    }

    @Test
    public void test_getProfileByUserId() {
//        Mono<ProfileResponse> res = Mono.create(s -> s.success(profileResponse1));
        Mono<ProfileResponse> res = Mono.just(profileResponse1);

        Mockito.when(commandExecutor.execute(GetProfileCommand.class, userId1))
                .thenReturn(res);

        Response<ProfileResponse> expected = new Response<>();
        expected.setData(profileResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        userController.getProfileByUserId(userId1)
                .subscribe(response -> {
                    Assert.assertEquals(response.getCode(), expected.getCode());
                    Assert.assertEquals(response.getStatus(), expected.getStatus());
                    Assert.assertEquals(response.getData().getName(), expected.getData().getName());
                    Assert.assertEquals(response.getData().getBirthDate(), expected.getData().getBirthDate());
                    Assert.assertEquals(response.getData().getGender(), expected.getData().getGender());
                    Assert.assertEquals(response.getData().getPhone(), expected.getData().getPhone());
                    Assert.assertEquals(response.getData().getUserId(), expected.getData().getUserId());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetProfileCommand.class, userId1);
    }

    @Test
    public void test_getAllUsers(){
        List<UserResponse> data = Arrays.asList(userResponse1, userResponse2);

        int size = 2;

        PagingRequest request = PagingRequest.builder().size(size).page(0).build();

        PagingResponse<UserResponse> pagingResponse = new PagingResponse<>(
                data, Paging.builder().itemPerPage(size).page(1).totalItem(2).totalPage(1).build()
        );

        Mono<PagingResponse<UserResponse>> response = Mono.just(pagingResponse);

        Mockito.when(commandExecutor.execute(GetAllUsersCommand.class, request))
                .thenReturn(response);

        Response<List<UserResponse>> expected = new Response<>();
        expected.setData(data);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());
        expected.setPaging(Paging.builder().totalPage(1).totalItem(2).itemPerPage(size).page(1).build());

        userController.getAllUsers(1, 2)
                .subscribe(listResponse -> {
                    Assert.assertEquals(listResponse.getCode(), expected.getCode());
                    Assert.assertEquals(listResponse.getStatus(), expected.getStatus());
                    for (int i=0; i < expected.getData().size(); i++) {
                        UserResponse res = listResponse.getData().get(i);
                        UserResponse ex = listResponse.getData().get(i);
                        Assert.assertEquals(res.getEmail(), ex.getEmail());
                        Assert.assertEquals(res.getIsActive(), ex.getIsActive());
                        Assert.assertEquals(res.getIsAdmin(), ex.getIsAdmin());
                        Assert.assertEquals(res.getIsBlocked(), ex.getIsBlocked());
                        Assert.assertEquals(res.getPhotoUrl(), ex.getPhotoUrl());
                        Assert.assertEquals(res.getUsername(), ex.getUsername());
                        Assert.assertEquals(res.getId(), ex.getId());
                    }
                });
        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetAllUsersCommand.class, request);
    }

    @Test
    public void test_updateProfile(){
        ProfileRequest request = ProfileRequest.builder()
                .name(profileResponse1.getName())
                .birthDate(dateString1)
                .gender(profileResponse1.getGender())
                .phone(profileResponse1.getPhone())
                .build();

        Mono<ProfileResponse> response = Mono.just(profileResponse1);

        Mockito.when(commandExecutor.execute(UpdateProfileCommand.class, request))
                .thenReturn(response);

        Response<ProfileResponse> expected = new Response<>();
        expected.setData(profileResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        userController.updateProfile(request, principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData().getName(), expected.getData().getName());
                    Assert.assertEquals(res.getData().getBirthDate(), expected.getData().getBirthDate());
                    Assert.assertEquals(res.getData().getGender(), expected.getData().getGender());
                    Assert.assertEquals(res.getData().getPhone(), expected.getData().getPhone());
                    Assert.assertEquals(res.getData().getUserId(), expected.getData().getUserId());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(UpdateProfileCommand.class, request);
    }

    @Test
    public void test_getMyProfile(){
        Mono<ProfileResponse> response = Mono.just(profileResponse1);

        Mockito.when(commandExecutor.execute(GetMyProfileCommand.class, principal1.getName()))
                .thenReturn(response);

        Response<ProfileResponse> expected = new Response<>();
        expected.setData(profileResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        userController.getMyProfile(principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData().getName(), expected.getData().getName());
                    Assert.assertEquals(res.getData().getBirthDate(), expected.getData().getBirthDate());
                    Assert.assertEquals(res.getData().getGender(), expected.getData().getGender());
                    Assert.assertEquals(res.getData().getPhone(), expected.getData().getPhone());
                    Assert.assertEquals(res.getData().getUserId(), expected.getData().getUserId());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetMyProfileCommand.class, principal1.getName());
    }

    @Test
    public void test_getUserPhoto() {
        byte[] data = new byte[]{};
        Mono<byte[]> response = Mono.just(data);
        Mockito.when(commandExecutor.execute(GetUserPhotoCommand.class, userId1))
                .thenReturn(response);

        userController.getPhoto(userId1)
                .subscribe(res -> {
                    Assert.assertEquals(res, data);
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(GetUserPhotoCommand.class, userId1);
    }

    @Test
    public void test_uploadUserPhoto() {
        ImageRequest request = new ImageRequest();
        request.setId(userId1);
        request.setRequester(principal1.getName());
        Mono<MessageResponse> response = Mono.just(MessageResponse.SUCCESS);
        Mockito.when(commandExecutor.execute(UploadUserPhotoCommand.class, request))
                .thenReturn(response);

        Response<MessageResponse> expected = new Response<>();
        expected.setData(MessageResponse.SUCCESS);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());


        userController.uploadPhoto(null, principal1)
                .subscribe(res -> {
                    Assert.assertEquals(res.getCode(), expected.getCode());
                    Assert.assertEquals(res.getStatus(), expected.getStatus());
                    Assert.assertEquals(res.getData(), expected.getData());
                });

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(UploadUserPhotoCommand.class, request);
    }

    @Test
    public void test_blockUser(){
        UserResponse userResponse3 = userResponse1;
        userResponse3.setIsBlocked(true);

        Mono<UserResponse> response = Mono.just(userResponse3);

        Mockito.when(commandExecutor.execute(BlockUserCommand.class, userId1))
                .thenReturn(response);

        Response<UserResponse> expected = new Response<>();
        expected.setData(userResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        userController.blockUser(userId1)
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

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(BlockUserCommand.class, userId1);
    }

    @Test
    public void test_unblockUser(){
        Mono<UserResponse> response = Mono.just(userResponse1);

        Mockito.when(commandExecutor.execute(UnblockUserCommand.class, userId1))
                .thenReturn(response);

        Response<UserResponse> expected = new Response<>();
        expected.setData(userResponse1);
        expected.setCode(HttpStatus.OK.value());
        expected.setStatus(HttpStatus.OK.name());

        userController.unblockUser(userId1)
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

        Mockito.verify(commandExecutor, Mockito.times(1)).execute(UnblockUserCommand.class, userId1);
    }

}
