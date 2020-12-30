package com.bliblifuturebackend.bliblimart.command.Impl;

import com.bliblifuturebackend.bliblimart.command.RegisterCommand;
import com.bliblifuturebackend.bliblimart.command.impl.RegisterCommandImpl;
import com.bliblifuturebackend.bliblimart.constant.UserConstant;
import com.bliblifuturebackend.bliblimart.model.entity.Profile;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.request.UserRequest;
import com.bliblifuturebackend.bliblimart.model.response.UserResponse;
import com.bliblifuturebackend.bliblimart.repository.ProfileRepository;
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
public class RegisterCommandImplTests {

    @TestConfiguration
    static class command{
        @Bean
        public RegisterCommand registerCommand(){
            return new RegisterCommandImpl();
        }
    }

    @Autowired
    private RegisterCommand registerCommand;

    @MockBean
    private ProfileRepository profileRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void test_execute() throws ParseException {
        String userId = "id123";
        String username = "user1";
        String pass = "pass1";
        String email = "user@mail.com";

        String encodePass = "encode1";

        UserRequest request = UserRequest.builder().username(username)
                .email(email).isAdmin(false).password(pass)
                .build();

        User user = User.builder().username(username)
                .email(email).isAdmin(false).password(encodePass)
                .build();

        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Mono.empty());

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(Mono.just(user));

        Mockito.when(profileRepository.save(Mockito.any(Profile.class)))
                .thenReturn(Mono.just(new Profile()));

        Mockito.when(passwordEncoder.encode(pass)).thenReturn(encodePass);

        UserResponse expected = user.createResponse();
        expected.setPhotoUrl(UserConstant.PHOTO_URL + userId);

        registerCommand.execute(request)
                .subscribe(res -> {
                    Assert.assertEquals(res.getUsername(), expected.getUsername());
                    Assert.assertEquals(res.getEmail(), expected.getEmail());
                    Assert.assertEquals(res.getIsAdmin(), expected.getIsAdmin());
                });

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verify(profileRepository, Mockito.times(1)).save(Mockito.any(Profile.class));
    }

//    @Test(expected = Exception.class)
//    public void test_execute_NPE() {
//        String userId = "id123";
//        String username = "user1";
//
//        User user = User.builder().username(username).build();
//        user.setId(userId);
//
//        String dateString1 = "2000-01-30";
//
//        ProfileRequest request = ProfileRequest.builder()
//                .name("profile1")
//                .phone("0123")
//                .gender(true)
//                .birthDate(dateString1)
//                .build();
//        request.setId("idProfile123");
//        request.setRequester(username);
//
//        Mockito.when(userRepository.findByUsername(username))
//                .thenReturn(Mono.just(user));
//
//        Mockito.when(profileRepository.findByUserId(userId))
//                .thenReturn(Mono.empty());
//
//        updateProfileCommand.execute(request).subscribe();
//
//        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
//        Mockito.verify(profileRepository, Mockito.times(1)).findByUserId(userId);
//        Mockito.verify(profileRepository, Mockito.times(0)).save(Mockito.any(Profile.class));
//    }

}
