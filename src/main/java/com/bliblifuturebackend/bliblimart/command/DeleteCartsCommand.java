package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.ListIdRequest;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;

public interface DeleteCartsCommand extends Command<ListIdRequest, MessageResponse> {

}
