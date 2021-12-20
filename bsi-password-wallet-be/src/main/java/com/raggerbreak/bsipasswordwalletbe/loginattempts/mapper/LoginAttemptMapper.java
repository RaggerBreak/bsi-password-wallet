package com.raggerbreak.bsipasswordwalletbe.loginattempts.mapper;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.dto.LoginAttemptDTO;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.model.LoginAttempt;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoginAttemptMapper {
    LoginAttemptDTO loginAttemptToDto(LoginAttempt loginAttempt);
}
