package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.response.TransactionResponse;

import java.util.List;

public interface GetUserActiveTransactionCommand extends Command<String, List<TransactionResponse>> {

}
