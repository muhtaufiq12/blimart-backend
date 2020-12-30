package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.BaseRequest;
import com.bliblifuturebackend.bliblimart.model.response.TransactionResponse;

public interface GetTransactionCommand extends Command<BaseRequest, TransactionResponse> {

}
