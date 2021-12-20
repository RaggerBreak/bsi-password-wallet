package com.raggerbreak.bsipasswordwalletbe.loginattempts.service;

public interface LoginAttemptService {

    void loginFailed(String remoteAddr, String username);
    void loginSucceeded(String remoteAddr, String username);
}
