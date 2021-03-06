package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.ImageRequest;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;

public interface UploadPhotoProductCommand extends Command<ImageRequest, MessageResponse> {

}
