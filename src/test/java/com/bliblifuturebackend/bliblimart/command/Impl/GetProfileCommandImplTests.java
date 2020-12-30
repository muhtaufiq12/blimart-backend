package com.bliblifuturebackend.bliblimart.command.Impl;

import com.bliblifuturebackend.bliblimart.command.GetProfileCommand;
import com.bliblifuturebackend.bliblimart.command.impl.GetProfileCommandImpl;
import com.bliblifuturebackend.bliblimart.model.entity.Profile;
import com.bliblifuturebackend.bliblimart.model.response.ProfileResponse;
import com.bliblifuturebackend.bliblimart.repository.ProfileRepository;
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
public class GetProfileCommandImplTests {

    @TestConfiguration
    static class command{
        @Bean
        public GetProfileCommand getProfileCommand(){
            return new GetProfileCommandImpl();
        }
    }

    @Autowired
    private GetProfileCommand getProfileCommand;

    @MockBean
    private ProfileRepository profileRepository;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void test_execute() throws ParseException {
        String userId = "id123";

        String dateString1 = "2000-01-30";

        Date date  = new SimpleDateFormat("yyyy-MM-dd").parse(dateString1);

        Profile profile = Profile.builder()
                .phone("0123").birthDate(date)
                .name("profile1").gender(true)
                .userId(userId).build();
        profile.setId("idProfile123");

        Mono<Profile> profileMono = Mono.just(profile);

        Mockito.when(profileRepository.findByUserId(userId))
                .thenReturn(profileMono);

        ProfileResponse expected = profile.createResponse();

        getProfileCommand.execute(userId)
                .subscribe(res -> {
                    Assert.assertEquals(res.getUserId(), expected.getUserId());
                    Assert.assertEquals(res.getPhone(), expected.getPhone());
                    Assert.assertEquals(res.getGender(), expected.getGender());
                    Assert.assertEquals(res.getBirthDate(), expected.getBirthDate());
                    Assert.assertEquals(res.getName(), expected.getName());
                    Assert.assertEquals(res.getId(), expected.getId());
                });

        Mockito.verify(profileRepository, Mockito.times(1)).findByUserId(userId);
    }

    @Test(expected = Exception.class)
    public void test_execute_NPE() {
        String userId = "id123";

        Mockito.when(profileRepository.findByUserId(userId))
                .thenReturn(Mono.empty());

        getProfileCommand.execute(userId).subscribe();

        Mockito.verify(profileRepository, Mockito.times(1)).findByUserId(userId);
    }

}
