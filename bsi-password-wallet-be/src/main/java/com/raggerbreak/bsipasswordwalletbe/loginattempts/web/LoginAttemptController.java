package com.raggerbreak.bsipasswordwalletbe.loginattempts.web;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.service.LoginAttemptService;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.web.response.LastLoginAttemptsLogsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/loginAttempt")
@RequiredArgsConstructor
public class LoginAttemptController {

    private final LoginAttemptService loginAttemptService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<LastLoginAttemptsLogsResponse> getLastLoginAttemptsLogsResponse(Principal principal) {
        return ResponseEntity.ok(loginAttemptService.getLastLoginAttemptsLogs(principal.getName()));
    }
}
