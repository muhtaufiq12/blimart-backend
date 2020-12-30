package com.bliblifuturebackend.bliblimart.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest extends BaseRequest{

    @Size(min = 1)
    @Valid
    private List<ProductTransactionRequest> products;
}
