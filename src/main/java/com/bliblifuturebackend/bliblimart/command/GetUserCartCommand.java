package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.response.CartWishlistResponse;

import java.util.List;

public interface GetUserCartCommand extends Command<String, List<CartWishlistResponse>> {

}
