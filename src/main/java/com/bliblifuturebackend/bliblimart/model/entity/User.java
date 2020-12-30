package com.bliblifuturebackend.bliblimart.model.entity;

import com.bliblifuturebackend.bliblimart.model.response.UserResponse;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = false)
@Document(collection = "user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User extends BaseEntity{

    @Field(name = "username")
    private String username;

    @Field(name = "password")
    private String password;

    @Field(name = "email")
    private String email;

    @Field(name = "is_admin")
    private Boolean isAdmin;

    @Field(name = "is_active")
    private Boolean isActive;

    @Field(name = "is_blocked")
    private Boolean isBlocked;

    @Field(name = "photo_url")
    private String photoUrl;

    public UserResponse createResponse(){
        return createResponse(this, new UserResponse());
    }
}

