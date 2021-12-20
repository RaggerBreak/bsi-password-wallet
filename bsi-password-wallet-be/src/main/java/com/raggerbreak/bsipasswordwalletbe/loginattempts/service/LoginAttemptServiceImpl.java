package com.raggerbreak.bsipasswordwalletbe.loginattempts.service;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.model.ELoginAttemptResult;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.model.LoginAttempt;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.repository.LoginAttemptRepository;
import com.raggerbreak.bsipasswordwalletbe.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final LoginAttemptRepository loginAttemptRepository;
    private final UserService userService;

    @Override
    public void loginFailed(String remoteAddr, String username) {
        loginAttemptRepository.save(LoginAttempt.builder()
                .ip(remoteAddr)
                .result(ELoginAttemptResult.FAILED)
                .username(username)
                .build());
    }

    @Override
    public void loginSucceeded(String remoteAddr, String username) {
        loginAttemptRepository.save(LoginAttempt.builder()
                .ip(remoteAddr)
                .result(ELoginAttemptResult.SUCCESSFUL)
                .username(username)
                .build());
    }
}
