package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.response.UserResponse;

public interface UnblockUserCommand extends Command<String, UserResponse> {
}
