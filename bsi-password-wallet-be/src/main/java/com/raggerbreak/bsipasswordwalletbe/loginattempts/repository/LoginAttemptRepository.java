package com.raggerbreak.bsipasswordwalletbe.loginattempts.repository;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.model.ELoginAttemptResult;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.model.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    LoginAttempt getFirstByUsernameAndResultOrderByTimestampDesc(String username, ELoginAttemptResult result);
}
