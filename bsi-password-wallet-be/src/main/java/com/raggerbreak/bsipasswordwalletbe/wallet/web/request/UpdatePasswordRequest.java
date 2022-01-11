package com.raggerbreak.bsipasswordwalletbe.wallet.web.request;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePasswordRequest implements Serializable {
    private static final long serialVersionUID = 6565402635626298050L;

    private String name;
    private String login;
    private String description;
    private String oldPassword;
    private String newPassword;
}
