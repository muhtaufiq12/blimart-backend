package com.bliblifuturebackend.bliblimart.model.entity;

import com.bliblifuturebackend.bliblimart.model.response.TransactionResponse;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = false)
@Document(collection = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends BaseEntity {

    @Field(name = "transaction_number")
    private String transactionNumber;

    @Field(name = "user_id")
    private String userId;

    @Field(name = "total")
    private long total;

    @Field(name = "status")
    private String status;

    public TransactionResponse createResponse(){
        return createResponse(this, new TransactionResponse());
    }
}
