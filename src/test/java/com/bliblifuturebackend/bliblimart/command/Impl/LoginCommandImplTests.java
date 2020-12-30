package com.bliblifuturebackend.bliblimart.command.Impl;

import com.bliblifuturebackend.bliblimart.command.LoginCommand;
import com.bliblifuturebackend.bliblimart.command.impl.LoginCommandImpl;
import com.bliblifuturebackend.bliblimart.config.JwtTokenUtil;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.request.UserRequest;
import com.bliblifuturebackend.bliblimart.model.response.JwtResponse;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.text.ParseException;

@RunWith(SpringRunner.class)
public class LoginCommandImplTests {

    @TestConfiguration
    static class command{
        @Bean
        public LoginCommand loginCommand(){
            return new LoginCommandImpl();
        }
    }

    @Autowired
    private LoginCommand loginCommand;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void test_execute() throws ParseException {
        String userId = "id123";
        String username = "user1";
        String pass = "pass1";
        String email = "user@mail.com";
        String encodePass = "encode1";

        String token = "token1";

        UserRequest request = UserRequest.builder()
                .username(username).password(pass)
                .build();

        User user = User.builder().username(username)
                .email(email).isAdmin(false).isActive(false).isBlocked(false).password(encodePass)
                .build();
        user.setId(userId);

        User activeUser = user;
        activeUser.setIsActive(true);

        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Mono.just(user));

        Mockito.when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);

        Mockito.when(jwtTokenUtil.generateToken(user)).thenReturn(token);

        Mockito.when(userRepository.save(activeUser)).thenReturn(Mono.just(activeUser));

        JwtResponse expected = JwtResponse.builder().userResponse(user.createResponse()).jwtToken(token).build();

        loginCommand.execute(request)
                .subscribe(res -> {
                    Assert.assertEquals(res.getJwtToken(), expected.getJwtToken());
                    Assert.assertEquals(res.getUserResponse(), expected.getUserResponse());
                });

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
        Mockito.verify(jwtTokenUtil, Mockito.times(1)).generateToken(user);
        Mockito.verify(passwordEncoder, Mockito.times(1)).matches(request.getPassword(), user.getPassword());
        Mockito.verify(userRepository, Mockito.times(1)).save(activeUser);
    }

}
