package com.raggerbreak.bsipasswordwalletbe.loginattempts.service;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.mapper.LoginAttemptMapper;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.model.ELoginAttemptResult;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.model.LoginAttempt;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.repository.LoginAttemptRepository;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.web.response.LastLoginAttemptsLogsResponse;
import com.raggerbreak.bsipasswordwalletbe.security.model.User;
import com.raggerbreak.bsipasswordwalletbe.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final LoginAttemptRepository loginAttemptRepository;
    private final LoginAttemptMapper loginAttemptMapper;
    private final UserService userService;

    @Override
    public User loginFailed(String remoteAddr, String username) {
        log.debug("LoginAttemptServiceImpl: loginFailed");
        if (userService.userExistsByUsername(username)) {
            loginAttemptRepository.save(LoginAttempt.builder()
                    .ip(remoteAddr)
                    .result(ELoginAttemptResult.FAILED)
                    .username(username)
                    .build());
        }
        return userService.incrementNumberOfFailedLoginAttemptsAndLockAccount(username);
    }

    @Override
    public void loginSucceeded(String remoteAddr, String username) {
        log.debug("LoginAttemptServiceImpl: loginSucceeded");
        if (userService.userExistsByUsername(username)) {
            loginAttemptRepository.save(LoginAttempt.builder()
                    .ip(remoteAddr)
                    .result(ELoginAttemptResult.SUCCESSFUL)
                    .username(username)
                    .build());
        }
        userService.resetNumberOfFailedLoginAttempts(username);
    }

    @Override
    public LastLoginAttemptsLogsResponse getLastLoginAttemptsLogs(String username) {
        log.debug("LoginAttemptServiceImpl: getLastLoginAttemptsLogs for username" + username);
        return LastLoginAttemptsLogsResponse.builder()
                .lastFailedLoginAttempt(loginAttemptMapper.loginAttemptToDto(loginAttemptRepository
                        .getFirstByUsernameAndResultOrderByTimestampDesc(username, ELoginAttemptResult.FAILED)))
                .lastSuccessfulLoginAttempt(loginAttemptMapper.loginAttemptToDto(loginAttemptRepository
                        .getFirstByUsernameAndResultOrderByTimestampDesc(username, ELoginAttemptResult.SUCCESSFUL)))
                .build();
    }
}
