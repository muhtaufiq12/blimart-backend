package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.DirectionRequest;
import com.bliblifuturebackend.bliblimart.model.response.RouteResponse;

public interface GetDirectionCommand extends Command<DirectionRequest, RouteResponse> {
}
