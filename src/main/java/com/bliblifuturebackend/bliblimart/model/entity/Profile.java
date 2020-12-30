package com.bliblifuturebackend.bliblimart.model.entity;

import com.bliblifuturebackend.bliblimart.model.response.ProfileResponse;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Document(collection = "profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile extends BaseEntity{

    @Field(name = "name")
    private String name;

    @Field(name = "birth_date")
    private Date birthDate;

    @Field(name = "gender")
    private Boolean gender;

    @Field(name = "phone")
    private String phone;

    @Field(name = "user_id")
    private String userId;

    public ProfileResponse createResponse(){
        return createResponse(this, new ProfileResponse());
    }
}

