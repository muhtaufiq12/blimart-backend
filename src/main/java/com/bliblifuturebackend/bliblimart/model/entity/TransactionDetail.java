package com.bliblifuturebackend.bliblimart.model.entity;

import com.bliblifuturebackend.bliblimart.model.response.TransactionDetailResponse;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = false)
@Document(collection = "transaction_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDetail extends BaseEntity {

    @Field(name = "transaction_detail_number")
    private String transactionDetailNumber;

    @Field(name = "product_id")
    private String productId;

    @Field(name = "sub_total")
    private long subTotal;

    @Field(name = "total_item")
    private long totalItem;

    @Field(name = "transaction_id")
    private String transactionId;

    @Field(name = "user_id")
    private String userId;

    @Field(name = "status")
    private String status;

    public TransactionDetailResponse createResponse(){
        return createResponse(this, new TransactionDetailResponse());
    }

}
