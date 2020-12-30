package com.bliblifuturebackend.bliblimart.command.Impl;

import com.bliblifuturebackend.bliblimart.command.BlockUserCommand;
import com.bliblifuturebackend.bliblimart.command.impl.BlockUserCommandImpl;
import com.bliblifuturebackend.bliblimart.model.entity.User;
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
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
public class BlockUserCommandImplTests {

    @TestConfiguration
    static class command{
        @Bean
        public BlockUserCommand blockUserCommand(){
            return new BlockUserCommandImpl();
        }
    }

    @Autowired
    private BlockUserCommand blockUserCommand;

    @MockBean
    private UserRepository userRepository;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void test_execute() {
        String userId = "id123";
        String username = "user1";

        User user = User.builder().username(username).isBlocked(false).isActive(true).build();
        user.setId(userId);

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Mono.just(user));;


        User userBlocked = User.builder().username(username).isBlocked(true).isActive(false).build();
        user.setId(userId);

        Mockito.when(userRepository.save(userBlocked))
                .thenReturn(Mono.just(userBlocked));

        UserResponse expected = userBlocked.createResponse();

        blockUserCommand.execute(userId)
                .subscribe(res -> {
                    Assert.assertEquals(res.getId(), expected.getId());
                    Assert.assertEquals(res.getUsername(), expected.getUsername());
                    Assert.assertEquals(res.getIsBlocked(), expected.getIsBlocked());
                    Assert.assertEquals(res.getIsActive(), expected.getIsActive());
                });

        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepository, Mockito.times(1)).save(userBlocked);
    }

//    @Test(expected = Exception.class)
//    public void test_execute_NPE() {
//        String userId = "id123";
//        String username = "user1";
//
//        User user = User.builder().username(username).build();
//        user.setId(userId);
//
//        Mockito.when(userRepository.findByUsername(username))
//                .thenReturn(Mono.just(user));
//
//        Mockito.when(profileRepository.findByUserId(userId))
//                .thenReturn(Mono.empty());
//
//
//        getMyProfileCommand.execute(username).subscribe();
//
//        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
//        Mockito.verify(profileRepository, Mockito.times(1)).findByUserId(userId);
//    }

}
