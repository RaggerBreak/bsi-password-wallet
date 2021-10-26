package com.raggerbreak.bsipasswordwalletbe.security.web;

import com.raggerbreak.bsipasswordwalletbe.security.service.AuthService;
import com.raggerbreak.bsipasswordwalletbe.security.web.request.LoginRequest;
import com.raggerbreak.bsipasswordwalletbe.security.web.request.SignupRequest;
import com.raggerbreak.bsipasswordwalletbe.security.web.response.JwtResponse;
import com.raggerbreak.bsipasswordwalletbe.security.web.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        return ResponseEntity.ok(authService.signin(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        MessageResponse messageResponse = authService.signup(signupRequest);

        if (messageResponse.isError())
            return ResponseEntity.badRequest().body(messageResponse);

        return ResponseEntity.ok(messageResponse);
    }
}


