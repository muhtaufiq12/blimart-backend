package com.bliblifuturebackend.bliblimart.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse extends BaseResponse{

    private String name;

    private Date birthDate;

    private Boolean gender;

    private String phone;

    private String userId;
}
