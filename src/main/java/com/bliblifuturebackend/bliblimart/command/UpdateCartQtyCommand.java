package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.CartWishlistRequest;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;

public interface UpdateCartQtyCommand extends Command<CartWishlistRequest, MessageResponse> {

}
