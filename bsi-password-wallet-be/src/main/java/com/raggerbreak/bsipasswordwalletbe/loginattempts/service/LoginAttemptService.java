package com.raggerbreak.bsipasswordwalletbe.loginattempts.service;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.web.response.LastLoginAttemptsLogsResponse;
import com.raggerbreak.bsipasswordwalletbe.security.model.User;

public interface LoginAttemptService {

    User loginFailed(String remoteAddr, String username);
    void loginSucceeded(String remoteAddr, String username);
    LastLoginAttemptsLogsResponse getLastLoginAttemptsLogs(String username);
}
