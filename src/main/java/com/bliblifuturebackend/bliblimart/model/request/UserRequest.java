package com.bliblifuturebackend.bliblimart.model.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest extends BaseRequest implements Serializable {

    @NotBlank
    @Length(min = 4, message = "minimal length is 4")
    private String username;

    @NotBlank
    @Length(min = 4, message = "minimal length is 4")
    private String password;

    @Email
    private String email;

    private boolean isAdmin;

}
