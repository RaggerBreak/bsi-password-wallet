package com.raggerbreak.bsipasswordwalletbe.wallet.web.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResponse implements Serializable {
    private static final long serialVersionUID = 8485363191945907736L;

    private String password;
}
