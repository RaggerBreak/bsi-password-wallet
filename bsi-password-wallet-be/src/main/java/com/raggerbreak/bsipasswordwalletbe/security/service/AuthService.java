package com.raggerbreak.bsipasswordwalletbe.security.service;

import com.raggerbreak.bsipasswordwalletbe.security.model.User;
import com.raggerbreak.bsipasswordwalletbe.security.web.request.SignupRequest;

public interface AuthService {

    User createUser(SignupRequest signupRequest);
}
