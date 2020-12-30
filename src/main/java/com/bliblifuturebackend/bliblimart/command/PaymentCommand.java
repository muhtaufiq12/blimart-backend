package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.PaymentRequest;
import com.bliblifuturebackend.bliblimart.model.response.TransactionResponse;

public interface PaymentCommand extends Command<PaymentRequest, TransactionResponse> {

}
