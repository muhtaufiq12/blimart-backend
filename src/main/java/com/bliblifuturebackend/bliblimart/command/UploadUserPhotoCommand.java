package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.ImageRequest;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;

public interface UploadUserPhotoCommand extends Command<ImageRequest, MessageResponse> {

}
