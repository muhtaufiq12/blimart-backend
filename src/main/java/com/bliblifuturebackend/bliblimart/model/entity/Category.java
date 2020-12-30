package com.bliblifuturebackend.bliblimart.model.entity;

import com.bliblifuturebackend.bliblimart.model.response.CategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseEntity{

    @Field(name = "name")
    private String name;

    public CategoryResponse createResponse(){
        return createResponse(this, new CategoryResponse());
    }

}
