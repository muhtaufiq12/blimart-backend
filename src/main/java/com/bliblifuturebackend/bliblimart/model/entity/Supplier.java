package com.bliblifuturebackend.bliblimart.model.entity;

import com.bliblifuturebackend.bliblimart.model.response.SupplierResponse;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = false)
@Document(collection = "supplier")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier extends BaseEntity{

    @Field(value = "name")
    private String name;

    @Field(value = "email")
    private String email;

    @Field(value = "phone")
    private String phone;

    public SupplierResponse createResponse(){
        return createResponse(this, new SupplierResponse());
    }

}

