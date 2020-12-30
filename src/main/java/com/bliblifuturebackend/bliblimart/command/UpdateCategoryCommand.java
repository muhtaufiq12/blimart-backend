package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.CategoryRequest;
import com.bliblifuturebackend.bliblimart.model.response.CategoryResponse;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;

public interface UpdateCategoryCommand extends Command<CategoryRequest, CategoryResponse> {

}
