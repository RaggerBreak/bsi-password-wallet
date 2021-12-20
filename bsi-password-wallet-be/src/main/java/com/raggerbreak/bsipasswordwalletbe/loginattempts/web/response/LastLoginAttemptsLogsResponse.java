package com.raggerbreak.bsipasswordwalletbe.loginattempts.web.response;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.dto.LoginAttemptDTO;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LastLoginAttemptsLogsResponse implements Serializable {
    private static final long serialVersionUID = -2923553654863581817L;

    private LoginAttemptDTO lastFailedLoginAttempt;
    private LoginAttemptDTO lastSuccessfulLoginAttempt;
}
