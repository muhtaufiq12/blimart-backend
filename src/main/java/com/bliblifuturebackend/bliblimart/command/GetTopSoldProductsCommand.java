package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
import com.bliblifuturebackend.bliblimart.model.response.ProductResponse;

import java.util.List;

public interface GetTopSoldProductsCommand extends Command<PagingRequest, List<ProductResponse>> {

}
