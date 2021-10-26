package com.raggerbreak.bsipasswordwalletbe.security.web.request;

import com.raggerbreak.bsipasswordwalletbe.security.model.PasswordForm;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class SignupRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    private String password;

    private PasswordForm passwordForm;
}
