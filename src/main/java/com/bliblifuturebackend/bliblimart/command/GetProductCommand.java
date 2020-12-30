package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.response.ProductResponse;

public interface GetProductCommand extends Command<String, ProductResponse> {

}
