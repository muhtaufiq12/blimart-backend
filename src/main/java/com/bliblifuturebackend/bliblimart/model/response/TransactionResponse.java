package com.bliblifuturebackend.bliblimart.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse extends BaseResponse {

    private String TransactionNumber;

    private List<TransactionDetailResponse> detailResponses;

    private long total;

    private String photoUrl;

    private String status;

    private UserResponse user;

}
