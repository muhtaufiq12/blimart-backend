package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.RouteRequest;
import com.bliblifuturebackend.bliblimart.model.response.RouteResponse;

public interface GetRouteCommand extends Command<RouteRequest, RouteResponse> {
}
