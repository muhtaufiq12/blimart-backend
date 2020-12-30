package com.bliblifuturebackend.bliblimart.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteRequest extends BaseRequest{

    @NotBlank
    @Length(min = 2, max = 2)
    private String startingMark;

    @NotBlank
    private String productId;
}
