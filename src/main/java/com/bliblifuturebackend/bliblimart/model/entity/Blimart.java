package com.bliblifuturebackend.bliblimart.model.entity;

import com.bliblifuturebackend.bliblimart.model.response.BlimartResponse;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = false)
@Document(collection = "blimart")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blimart extends BaseEntity{

    @Field(name = "name")
    private String name;

    @Field(name = "x_coordinate")
    private float x;

    @Field(name = "y_coordinate")
    private float y;

    public BlimartResponse createResponse(){
        return createResponse(this, new BlimartResponse());
    }
}

