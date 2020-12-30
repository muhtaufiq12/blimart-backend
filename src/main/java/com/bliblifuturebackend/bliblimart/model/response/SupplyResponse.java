package com.bliblifuturebackend.bliblimart.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplyResponse extends BaseResponse{

    private SupplierResponse supplierResponse;

    private ProductResponse productResponse;

    private long qty;
}
