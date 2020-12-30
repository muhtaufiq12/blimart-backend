package com.bliblifuturebackend.bliblimart.model.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class JwtResponse implements Serializable {

    private final String jwtToken;

    private UserResponse userResponse;

}
