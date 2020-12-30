package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.response.BlimartResponse;

import java.util.List;

public interface GetAllBlimartsCommand extends Command<String, List<BlimartResponse>> {

}
