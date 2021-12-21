package com.raggerbreak.bsipasswordwalletbe.security.service;

import com.raggerbreak.bsipasswordwalletbe.security.model.User;
import com.raggerbreak.bsipasswordwalletbe.security.web.request.ChangeUserPasswordRequest;
import com.raggerbreak.bsipasswordwalletbe.security.web.response.MessageResponse;

public interface UserService {
    MessageResponse changePassword(ChangeUserPasswordRequest request) throws Exception;
    User getCurrentAuthUser();
    boolean userExistsByUsername(String username);
    User incrementNumberOfFailedLoginAttemptsAndLockAccount(String username);
    void resetNumberOfFailedLoginAttempts(String username);
    boolean checkIfLockTimeExpiredThenUnlockAccount(String username);
}
