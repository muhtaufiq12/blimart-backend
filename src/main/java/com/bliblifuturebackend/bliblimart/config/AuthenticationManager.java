package com.bliblifuturebackend.bliblimart.config;

import com.blibli.oss.command.CommandExecutor;
import com.bliblifuturebackend.bliblimart.command.GetUserDetailsByUsernameCommand;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    CommandExecutor commandExecutor;

    @Autowired
    private UserRepository userRepository;

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        String username = jwtTokenUtil.getUsernameFromToken(token);
        return commandExecutor.execute(GetUserDetailsByUsernameCommand.class, username)
                            .flatMap(user -> isValid(token,user));
    }

    public Mono<Authentication> isValid(String token, User user){
        if(jwtTokenUtil.validateToken(token)){
            if(!user.getIsActive() || user.getIsBlocked()){
                return Mono.empty();
            }
            setIsActive(user, true);
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            if(user.getIsAdmin()){
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
            return Mono.just(new UsernamePasswordAuthenticationToken(jwtTokenUtil.getUsernameFromToken(token), null, authorities));
        }
        setIsActive(user, false);
        return Mono.empty();
    }

    private void setIsActive(User user, boolean isActive) {
        user.setIsActive(isActive);
        userRepository.save(user).subscribe();
    }
}
