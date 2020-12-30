package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.response.CategoryResponse;

import java.util.List;

public interface GetAllCategoriesCommand extends Command<String, List<CategoryResponse>> {

}
