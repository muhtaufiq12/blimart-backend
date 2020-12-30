package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.MarkRequest;
import com.bliblifuturebackend.bliblimart.model.response.MarkResponse;

public interface CreateNewMarkCommand extends Command<MarkRequest, MarkResponse> {

}
