package com.bliblifuturebackend.bliblimart.command.Impl;

import com.blibli.oss.common.paging.Paging;
import com.bliblifuturebackend.bliblimart.command.GetAllUsersCommand;
import com.bliblifuturebackend.bliblimart.command.impl.GetAllUsersCommandImpl;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
import com.bliblifuturebackend.bliblimart.model.response.PagingResponse;
import com.bliblifuturebackend.bliblimart.model.response.UserResponse;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
public class GetAllUsersCommandImplTests {

    @TestConfiguration
    static class command{
        @Bean
        public GetAllUsersCommand getAllUsersCommand(){
            return new GetAllUsersCommandImpl();
        }
    }

    @Autowired
    private GetAllUsersCommand getAllUsersCommand;

    @MockBean
    private UserRepository userRepository;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void test_execute() throws ParseException {
        User user1 = User.builder()
                .username("username1")
                .password("pass1")
                .photoUrl("/user-photo/userId1")
                .email("user1@mail.com")
                .isAdmin(false)
                .isBlocked(false)
                .isActive(false)
                .build();
        user1.setId("userId1");

        User user2 = User.builder()
                .username("username2")
                .password("pass2")
                .photoUrl("/user-photo/userId2")
                .email("user2@mail.com")
                .isAdmin(false)
                .isBlocked(false)
                .isActive(false)
                .build();
        user2.setId("userId2");

        long totalItem = 2;
        int totalPage = 1;
        int size = 2;
        int page = 1;

        PagingRequest request = PagingRequest.builder().size(size).page(page-1).build();

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Mockito.when(userRepository.findAll(pageable))
                .thenReturn(Flux.just(user1, user2));

        Mockito.when(userRepository.count())
                .thenReturn(Mono.just(totalItem));

        List<UserResponse> data = Arrays.asList(user1.createResponse(), user2.createResponse());

        PagingResponse<UserResponse> expected = new PagingResponse<>();
        expected.setData(data);
        expected.setPaging(Paging.builder().page(page).itemPerPage(size).totalItem((int) totalItem).totalPage(totalPage).build());

        getAllUsersCommand.execute(request)
                .subscribe(response -> {
                    Paging exPaging = expected.getPaging();
                    Paging resPaging = response.getPaging();
                    Assert.assertEquals(exPaging.getPage(), resPaging.getPage());
                    Assert.assertEquals(exPaging.getTotalItem(), resPaging.getTotalItem());
                    Assert.assertEquals(exPaging.getItemPerPage(), resPaging.getItemPerPage());
                    Assert.assertEquals(exPaging.getTotalPage(), resPaging.getTotalPage());

                    for (int i = 0; i < expected.getData().size(); i++) {
                        UserResponse exData = expected.getData().get(i);
                        UserResponse resData = response.getData().get(i);
                        Assert.assertEquals(exData.getEmail(), resData.getEmail());
                        Assert.assertEquals(exData.getIsActive(), resData.getIsActive());
                        Assert.assertEquals(exData.getIsAdmin(), resData.getIsAdmin());
                        Assert.assertEquals(exData.getIsBlocked(), resData.getIsBlocked());
                        Assert.assertEquals(exData.getPhotoUrl(), resData.getPhotoUrl());
                        Assert.assertEquals(exData.getUsername(), resData.getUsername());
                        Assert.assertEquals(exData.getId(), resData.getId());
                    }
                });

        Mockito.verify(userRepository, Mockito.times(1)).findAll(pageable);
        Mockito.verify(userRepository, Mockito.times(1)).count();
    }

//    @Test(expected = Exception.class)
//    public void test_execute_NPE() {
//        String userId = "id123";
//
//        Mockito.when(profileRepository.findByUserId(userId))
//                .thenReturn(Mono.empty());
//
//        getProfileCommand.execute(userId).subscribe();
//
//        Mockito.verify(profileRepository, Mockito.times(1)).findByUserId(userId);
//    }

}
