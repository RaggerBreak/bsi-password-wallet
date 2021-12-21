package com.raggerbreak.bsipasswordwalletbe.loginattempts.listener;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.service.IpAddressLockService;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFailureLockedListener implements ApplicationListener<AuthenticationFailureLockedEvent> {

    private final HttpServletRequest request;
    private final LoginAttemptService loginAttemptService;
    private final IpAddressLockService ipAddressLockService;

    @Override
    public void onApplicationEvent(AuthenticationFailureLockedEvent event) {
        log.debug("Handling AuthenticationFailureLockedEvent");

        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            loginAttemptService.loginFailed(request.getRemoteAddr(), event.getAuthentication().getName());
            ipAddressLockService.loginFailed(request.getRemoteAddr(), event.getAuthentication().getName());
        } else {
            loginAttemptService.loginFailed(xfHeader.split(",")[0], event.getAuthentication().getName());
            ipAddressLockService.loginFailed(xfHeader.split(",")[0], event.getAuthentication().getName());
        }
    }
}
