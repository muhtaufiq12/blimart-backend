package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.SupplierRequest;
import com.bliblifuturebackend.bliblimart.model.response.SupplierResponse;

public interface CreateSupplierCommand extends Command<SupplierRequest, SupplierResponse> {

}
