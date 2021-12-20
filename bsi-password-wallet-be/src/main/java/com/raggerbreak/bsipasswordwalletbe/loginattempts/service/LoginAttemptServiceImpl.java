package com.raggerbreak.bsipasswordwalletbe.loginattempts.service;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.mapper.LoginAttemptMapper;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.model.ELoginAttemptResult;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.model.LoginAttempt;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.repository.LoginAttemptRepository;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.web.response.LastLoginAttemptsLogsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final LoginAttemptRepository loginAttemptRepository;
    private final LoginAttemptMapper loginAttemptMapper;

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

    @Override
    public LastLoginAttemptsLogsResponse getLastLoginAttemptsLogs(String username) {
        return LastLoginAttemptsLogsResponse.builder()
                .lastFailedLoginAttempt(loginAttemptMapper.loginAttemptToDto(loginAttemptRepository
                        .getFirstByUsernameAndResultOrderByTimestampDesc(username, ELoginAttemptResult.FAILED)))
                .lastSuccessfulLoginAttempt(loginAttemptMapper.loginAttemptToDto(loginAttemptRepository
                        .getFirstByUsernameAndResultOrderByTimestampDesc(username, ELoginAttemptResult.SUCCESSFUL)))
                .build();
    }
}
