package com.raggerbreak.bsipasswordwalletbe.security.web.request;

import com.raggerbreak.bsipasswordwalletbe.security.model.PasswordForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangeUserPasswordRequest {

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;

    private PasswordForm passwordForm;
}
