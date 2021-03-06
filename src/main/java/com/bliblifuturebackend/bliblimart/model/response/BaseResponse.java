package com.bliblifuturebackend.bliblimart.model.response;

import lombok.Data;

import java.util.Date;

@Data
public abstract class BaseResponse {

    private String id;

    private Date createdDate;

    private String createdBy;

    private Date updatedDate;

    private String updatedBy;
}
