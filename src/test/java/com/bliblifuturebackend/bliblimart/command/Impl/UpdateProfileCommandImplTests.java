package com.bliblifuturebackend.bliblimart.command.Impl;

import com.bliblifuturebackend.bliblimart.command.UpdateProfileCommand;
import com.bliblifuturebackend.bliblimart.command.impl.UpdateProfileCommandImpl;
import com.bliblifuturebackend.bliblimart.model.entity.Profile;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.request.ProfileRequest;
import com.bliblifuturebackend.bliblimart.model.response.ProfileResponse;
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
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
public class UpdateProfileCommandImplTests {

    @TestConfiguration
    static class command{
        @Bean
        public UpdateProfileCommand updateProfileCommand(){
            return new UpdateProfileCommandImpl();
        }
    }

    @Autowired
    private UpdateProfileCommand updateProfileCommand;

    @MockBean
    private ProfileRepository profileRepository;

    @MockBean
    private UserRepository userRepository;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void test_execute() throws ParseException {
        String userId = "id123";
        String username = "user1";

        User user = User.builder().username(username).build();
        user.setId(userId);

        String dateString1 = "2000-01-30";

        ProfileRequest request = ProfileRequest.builder()
                .name("profile1")
                .phone("0123")
                .gender(true)
                .birthDate(dateString1)
                .build();
        request.setId("idProfile123");
        request.setRequester(username);

        Date date  = new SimpleDateFormat("yyyy-MM-dd").parse(dateString1);

        Profile profile = Profile.builder()
                .phone("0123")
                .birthDate(date)
                .name("profile1")
                .gender(true)
                .userId(userId)
                .build();
        profile.setId("idProfile123");

        Mono<Profile> profileMono = Mono.just(profile);

        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Mono.just(user));

        Mockito.when(profileRepository.findByUserId(userId))
                .thenReturn(profileMono);

        Mockito.when(profileRepository.save(profile))
                .thenReturn(profileMono);

        ProfileResponse expected = profile.createResponse();

        updateProfileCommand.execute(request)
                .subscribe(res -> {
                    Assert.assertEquals(res.getUserId(), expected.getUserId());
                    Assert.assertEquals(res.getPhone(), expected.getPhone());
                    Assert.assertEquals(res.getGender(), expected.getGender());
                    Assert.assertEquals(res.getBirthDate(), expected.getBirthDate());
                    Assert.assertEquals(res.getName(), expected.getName());
                    Assert.assertEquals(res.getId(), expected.getId());
                });

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
        Mockito.verify(profileRepository, Mockito.times(1)).findByUserId(userId);
        Mockito.verify(profileRepository, Mockito.times(1)).save(profile);
    }

    @Test(expected = Exception.class)
    public void test_execute_NPE() {
        String userId = "id123";
        String username = "user1";

        User user = User.builder().username(username).build();
        user.setId(userId);

        String dateString1 = "2000-01-30";

        ProfileRequest request = ProfileRequest.builder()
                .name("profile1")
                .phone("0123")
                .gender(true)
                .birthDate(dateString1)
                .build();
        request.setId("idProfile123");
        request.setRequester(username);

        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Mono.just(user));

        Mockito.when(profileRepository.findByUserId(userId))
                .thenReturn(Mono.empty());

        updateProfileCommand.execute(request).subscribe();

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
        Mockito.verify(profileRepository, Mockito.times(1)).findByUserId(userId);
        Mockito.verify(profileRepository, Mockito.times(0)).save(Mockito.any(Profile.class));
    }

}
