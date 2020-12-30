package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.CategoryRequest;
import com.bliblifuturebackend.bliblimart.model.response.CategoryResponse;

public interface CreateCategoryCommand extends Command<CategoryRequest, CategoryResponse> {

}
