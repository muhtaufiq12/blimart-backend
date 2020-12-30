package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
import com.bliblifuturebackend.bliblimart.model.response.CartWishlistResponse;
import com.bliblifuturebackend.bliblimart.model.response.PagingResponse;

public interface GetUserWishlistCommand extends Command<PagingRequest, PagingResponse<CartWishlistResponse>> {

}
