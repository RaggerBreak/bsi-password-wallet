package com.raggerbreak.bsipasswordwalletbe.loginattempts.web;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.dto.IpAddressLockDTO;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.service.IpAddressLockService;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.service.LoginAttemptService;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.web.response.LastLoginAttemptsLogsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/loginAttempt")
@RequiredArgsConstructor
public class LoginAttemptController {

    private final LoginAttemptService loginAttemptService;
    private final IpAddressLockService ipAddressLockService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<LastLoginAttemptsLogsResponse> getLastLoginAttemptsLogsResponse(Principal principal) {
        return ResponseEntity.ok(loginAttemptService.getLastLoginAttemptsLogs(principal.getName()));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/permanentlyLockedIp")
    public ResponseEntity<List<IpAddressLockDTO>> getPermanentlyLockedIpAddresses(Principal principal) {
        return ResponseEntity.ok(ipAddressLockService.getPermanentlyLockedIpAddresses(principal.getName()));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/permanentlyLockedIp/{ipAddressId}")
    public void deletePermanentlyLockedIpAddress(@PathVariable Long ipAddressId, Principal principal) {
        ipAddressLockService.deletePermanentlyLockedIpAddress(ipAddressId, principal.getName());
    }
}
