package com.bliblifuturebackend.bliblimart.model.entity;

import com.bliblifuturebackend.bliblimart.model.response.CartWishlistResponse;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@EqualsAndHashCode(callSuper = false)
@Document(collection = "wishlist")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wishlist extends BaseEntity{

    @Field(name = "product_id")
    private String productId;

    @Field(name = "user_id")
    private String userId;

    public CartWishlistResponse createResponse(){
        return createResponse(this, new CartWishlistResponse());
    }
}

