package com.raggerbreak.bsipasswordwalletbe.loginattempts.listener;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent>  {

    private final HttpServletRequest request;
    private final LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            loginAttemptService.loginSucceeded(request.getRemoteAddr(), event.getAuthentication().getName());
        } else {
            loginAttemptService.loginSucceeded(xfHeader.split(",")[0], event.getAuthentication().getName());
        }
    }
}
