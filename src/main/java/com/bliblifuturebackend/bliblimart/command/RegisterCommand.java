package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.UserRequest;
import com.bliblifuturebackend.bliblimart.model.response.UserResponse;

public interface RegisterCommand extends Command<UserRequest, UserResponse> {

}
