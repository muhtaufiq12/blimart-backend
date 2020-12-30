package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.UserRequest;
import com.bliblifuturebackend.bliblimart.model.response.JwtResponse;

public interface LoginCommand extends Command<UserRequest, JwtResponse> {

}
