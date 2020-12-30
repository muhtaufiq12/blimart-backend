package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.ProductRequest;
import com.bliblifuturebackend.bliblimart.model.response.ProductResponse;

public interface CreateProductCommand extends Command<ProductRequest, ProductResponse> {

}
