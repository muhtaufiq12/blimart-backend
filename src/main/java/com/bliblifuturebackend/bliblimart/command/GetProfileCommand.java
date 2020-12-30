package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.ProfileRequest;
import com.bliblifuturebackend.bliblimart.model.response.ProfileResponse;

public interface GetProfileCommand extends Command<String, ProfileResponse> {

}