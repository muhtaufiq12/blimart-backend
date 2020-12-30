package com.bliblifuturebackend.bliblimart.model.entity;

import com.bliblifuturebackend.bliblimart.model.response.ProductResponse;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = false)
@Document(collection = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity{

    @Field(name = "name")
    private String name;

    @Field(name = "price")
    private long price;

    @Field(name = "discount_price")
    private long discountPrice;

    @Field(name = "discount")
    private long discount;

    @Field(name = "photo_url")
    private String photoUrl;

    @Field(name = "mark1")
    private String mark1;

    @Field(name = "mark2")
    private String mark2;

    @Field(name = "variant")
    private String variant;

    @Field(name = "category_id")
    private String categoryId;

    @Field(name = "blimart_id")
    private String blimartId;

    @Field(name = "stock")
    private long stock;

    @Field(name = "total_sold")
    private long totalSold;

    public ProductResponse createResponse(){
        return createResponse(this, new ProductResponse());
    }
}

