package com.bliblifuturebackend.bliblimart.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse extends BaseResponse {

    private String name;

    private long price;

    private long discountPrice;

    private long stock;

    private long discount;

    private String photoUrl;

    private String mark1;

    private String mark2;

    private String variant;

    private String category;

    private long totalSold;

    private BlimartResponse blimartResponse;
}
