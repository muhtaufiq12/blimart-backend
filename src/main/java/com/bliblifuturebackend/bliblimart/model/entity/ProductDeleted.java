package com.bliblifuturebackend.bliblimart.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "product_deleted")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDeleted {

    @Field(name = "name")
    private String name;

    @Field(name = "price")
    private long price;

    @Field(name = "discount")
    private long discount;

    @Field(name = "photo_url")
    private String photoUrl;

    @Field(name = "x_coordinate")
    private float xCoordinate;

    @Field(name = "y_coordinate")
    private float yCoordinate;

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

    @Id
    @Field(name = "id")
    private String id;

    @Field(value = "deleted_by")
    private String deletedBy;

    @Field(value = "deleted_date")
    private Date deletedDate;

}

