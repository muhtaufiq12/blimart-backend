package com.bliblifuturebackend.bliblimart.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "supplier_deleted")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierDeleted{

    @Field(value = "name")
    private String name;

    @Field(value = "email")
    private String email;

    @Field(value = "phone")
    private String phone;

    @Id
    @Field(name = "id")
    private String id;

    @Field(value = "deleted_by")
    private String deletedBy;

    @Field(value = "deleted_date")
    private Date deletedDate;

}

