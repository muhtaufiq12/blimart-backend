package com.bliblifuturebackend.bliblimart.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse extends BaseResponse{

    private String username;

    private String email;

    private Boolean isAdmin;

    private Boolean isActive;

    private Boolean isBlocked;

    private String photoUrl;
}
