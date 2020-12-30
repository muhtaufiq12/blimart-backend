package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.bliblifuturebackend.bliblimart.command.LoginCommand;
import com.bliblifuturebackend.bliblimart.command.LogoutCommand;
import com.bliblifuturebackend.bliblimart.command.RefreshAuthCommand;
import com.bliblifuturebackend.bliblimart.command.RegisterCommand;
import com.bliblifuturebackend.bliblimart.constant.ApiConstant;
import com.bliblifuturebackend.bliblimart.model.request.UserRequest;
import com.bliblifuturebackend.bliblimart.model.response.JwtResponse;
import com.bliblifuturebackend.bliblimart.model.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.security.Principal;

@RestController
@RequestMapping(ApiConstant.BASE_API)
public class AuthController {

    @Autowired
    private CommandExecutor commandExecutor;

    @PostMapping(value = "/register")
    public Mono<Response<UserResponse>> register(@RequestBody UserRequest request){
        return commandExecutor.execute(RegisterCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping(value = "/login")
    public Mono<Response<JwtResponse>> login(@RequestBody UserRequest request){
        return commandExecutor.execute(LoginCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping(value = "/refresh")
    public Mono<Response<JwtResponse>> refresh(Principal principal){
        return commandExecutor.execute(RefreshAuthCommand.class, principal)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping(value = "/logout")
    public Mono<Response<JwtResponse>> logout(Principal principal){
        return commandExecutor.execute(LogoutCommand.class, principal)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

}
