package com.raggerbreak.bsipasswordwalletbe.loginattempts.listener;

import com.raggerbreak.bsipasswordwalletbe.exceptions.IpLockedException;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.service.IpAddressLockService;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.service.LoginAttemptService;
import com.raggerbreak.bsipasswordwalletbe.security.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
            User user = loginAttemptService.loginFailed(request.getRemoteAddr(), event.getAuthentication().getName());
            ipAddressLockService.loginFailed(request.getRemoteAddr(), event.getAuthentication().getName());
            checkAndThrowError(user);
        } else {
            User user = loginAttemptService.loginFailed(xfHeader.split(",")[0], event.getAuthentication().getName());
            ipAddressLockService.loginFailed(xfHeader.split(",")[0], event.getAuthentication().getName());
            checkAndThrowError(user);
        }
    }

    private void checkAndThrowError(User user) {
        if (Objects.nonNull(user) && Objects.nonNull(user.getLockTime())) {
            long millis = user.getLockTime().getTime() - new Date().getTime();
            throw new IpLockedException("Account is locked. Please try again in " + String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
            ));
        }
    }
}
