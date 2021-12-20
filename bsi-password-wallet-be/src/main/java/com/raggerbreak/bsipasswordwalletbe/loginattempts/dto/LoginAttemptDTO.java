package com.raggerbreak.bsipasswordwalletbe.loginattempts.dto;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.model.ELoginAttemptResult;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginAttemptDTO implements Serializable {
    private static final long serialVersionUID = 6523273180722842118L;

    private String ip;
    private ELoginAttemptResult result;
    private Date timestamp;
    private String username;
}
