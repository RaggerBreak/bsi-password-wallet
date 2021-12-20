package com.raggerbreak.bsipasswordwalletbe.loginattempts.service;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.web.response.LastLoginAttemptsLogsResponse;

public interface LoginAttemptService {

    void loginFailed(String remoteAddr, String username);
    void loginSucceeded(String remoteAddr, String username);
    LastLoginAttemptsLogsResponse getLastLoginAttemptsLogs(String username);
}
