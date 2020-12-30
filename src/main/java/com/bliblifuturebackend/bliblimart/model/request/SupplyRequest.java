package com.bliblifuturebackend.bliblimart.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplyRequest extends BaseRequest{

    @NotBlank
    private String supplierId;

    @NotBlank
    private String productId;

    @Min(1)
    @Digits(integer = 5, fraction = 0)
    private long qty;
}
