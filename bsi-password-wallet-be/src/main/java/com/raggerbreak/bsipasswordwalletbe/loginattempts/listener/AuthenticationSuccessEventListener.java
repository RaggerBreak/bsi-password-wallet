package com.raggerbreak.bsipasswordwalletbe.loginattempts.listener;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.service.IpAddressLockService;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent>  {

    private final HttpServletRequest request;
    private final LoginAttemptService loginAttemptService;
    private final IpAddressLockService ipAddressLockService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        log.debug("Handling AuthenticationSuccessEvent");

        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            loginAttemptService.loginSucceeded(request.getRemoteAddr(), event.getAuthentication().getName());
            ipAddressLockService.loginSucceeded(request.getRemoteAddr(), event.getAuthentication().getName());
        } else {
            loginAttemptService.loginSucceeded(xfHeader.split(",")[0], event.getAuthentication().getName());
            ipAddressLockService.loginSucceeded(xfHeader.split(",")[0], event.getAuthentication().getName());
        }
    }
}
