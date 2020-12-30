package com.bliblifuturebackend.bliblimart.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest extends BaseRequest{

    @NotBlank
    private String name;

    @Min(100)
    @Max(100_000_000)
    private long price;

    private String variant;

    @Min(0)
    private long stock;

    @Min(0)
    @Max(99)
    private long discount;

    @NotBlank
    @Length(min = 2, max = 2)
    private String mark1;

    @NotBlank
    @Length(min = 2, max = 2)
    private String mark2;

    @NotBlank
    private String categoryId;

    @NotBlank
    private String blimartId;
}
