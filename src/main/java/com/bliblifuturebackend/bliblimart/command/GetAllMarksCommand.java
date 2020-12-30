package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.response.MarkResponse;

import java.util.List;

public interface GetAllMarksCommand extends Command<String, List<MarkResponse>> {

}
