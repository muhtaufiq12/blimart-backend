package com.bliblifuturebackend.bliblimart.command.Impl;

import com.bliblifuturebackend.bliblimart.command.LogoutCommand;
import com.bliblifuturebackend.bliblimart.command.impl.LogoutCommandImpl;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.response.JwtResponse;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.text.ParseException;

@RunWith(SpringRunner.class)
public class LogoutCommandImplTests {

    @TestConfiguration
    static class command{
        @Bean
        public LogoutCommand logoutCommand(){
            return new LogoutCommandImpl();
        }
    }

    @Autowired
    private LogoutCommand logoutCommand;

    @MockBean
    private UserRepository userRepository;

    Principal principal;

    @Test
    public void test_execute() throws ParseException {
        String userId = "id123";
        String username = "user1";
        String email = "user@mail.com";
        String encodePass = "encode1";

        principal = new Principal() {
            @Override
            public String getName() {
                return username;
            }
        };

        User user = User.builder().username(username)
                .email(email).isAdmin(false).isActive(true).isBlocked(false).password(encodePass)
                .build();
        user.setId(userId);

        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Mono.just(user));

        User logoutUser = user;
        logoutUser.setIsActive(false);
        Mockito.when(userRepository.save(logoutUser))
                .thenReturn(Mono.just(logoutUser));

        JwtResponse expected = JwtResponse.builder().build();

        logoutCommand.execute(principal)
                .subscribe(res -> {
                    Assert.assertEquals(res, expected);
                });

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
        Mockito.verify(userRepository, Mockito.times(1)).save(logoutUser);
    }

}
