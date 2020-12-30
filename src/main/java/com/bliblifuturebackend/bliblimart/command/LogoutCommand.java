package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.response.JwtResponse;

import java.security.Principal;

public interface LogoutCommand extends Command<Principal, JwtResponse> {

}
