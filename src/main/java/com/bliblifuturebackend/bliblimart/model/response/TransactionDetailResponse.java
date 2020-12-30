package com.bliblifuturebackend.bliblimart.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailResponse extends BaseResponse {

    private String TransactionDetailNumber;

    private ProductResponse productResponse;

    private long subTotal;

    private String status;

    private long totalItem;
}
