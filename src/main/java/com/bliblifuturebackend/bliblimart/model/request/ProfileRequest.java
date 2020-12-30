package com.bliblifuturebackend.bliblimart.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest extends BaseRequest{

    @NotBlank
    private String name;

    @Length(min = 10, max = 10)
    private String birthDate;

    private Boolean gender;

    @Digits(integer = 15, fraction = 0)
    private String phone;
}
