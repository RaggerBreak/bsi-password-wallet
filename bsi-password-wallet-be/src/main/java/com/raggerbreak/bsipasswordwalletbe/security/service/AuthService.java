package com.raggerbreak.bsipasswordwalletbe.security.service;

import com.raggerbreak.bsipasswordwalletbe.security.web.request.LoginRequest;
import com.raggerbreak.bsipasswordwalletbe.security.web.request.SignupRequest;
import com.raggerbreak.bsipasswordwalletbe.security.web.response.JwtResponse;
import com.raggerbreak.bsipasswordwalletbe.security.web.response.MessageResponse;

public interface AuthService {

    JwtResponse signin(LoginRequest loginRequest);
    MessageResponse signup(SignupRequest signupRequest);
}
