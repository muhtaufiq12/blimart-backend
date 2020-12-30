package com.bliblifuturebackend.bliblimart.model.entity;

import com.bliblifuturebackend.bliblimart.model.response.SupplyResponse;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = false)
@Document(collection = "supply")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supply extends BaseEntity {

    @Field(name = "supplier_id")
    private String supplierId;

    @Field(name = "product_id")
    private String productId;

    @Field(name = "qty")
    private long qty;

    public SupplyResponse createResponse(){
        return createResponse(this, new SupplyResponse());
    }
}
