package com.bliblifuturebackend.bliblimart.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartWishlistRequest extends BaseRequest{

    @NotBlank
    private String productId;

    private long totalItem;
}
