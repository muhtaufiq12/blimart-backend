package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.TransactionRequest;
import com.bliblifuturebackend.bliblimart.model.response.TransactionResponse;

public interface CreateTransactionCommand extends Command<TransactionRequest, TransactionResponse> {

}
