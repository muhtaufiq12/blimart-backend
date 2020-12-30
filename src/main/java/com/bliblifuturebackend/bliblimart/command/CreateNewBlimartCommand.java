package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.BlimartRequest;
import com.bliblifuturebackend.bliblimart.model.response.BlimartResponse;

public interface CreateNewBlimartCommand extends Command<BlimartRequest, BlimartResponse> {

}
