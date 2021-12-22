package com.raggerbreak.bsipasswordwalletbe.loginattempts.service;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.dto.LoginAttemptDTO;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.mapper.LoginAttemptMapper;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.model.ELoginAttemptResult;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.model.LoginAttempt;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.repository.LoginAttemptRepository;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.web.response.LastLoginAttemptsLogsResponse;
import com.raggerbreak.bsipasswordwalletbe.security.model.User;
import com.raggerbreak.bsipasswordwalletbe.security.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoginAttemptServiceImplTest {

    @Mock
    LoginAttemptRepository loginAttemptRepository;
    @Mock
    LoginAttemptMapper loginAttemptMapper;
    @Mock
    UserService userService;

    LoginAttemptService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new LoginAttemptServiceImpl(loginAttemptRepository, loginAttemptMapper, userService);
    }

    @Test
    void testLoginFailed_userExists() {
        //given
        String remoteAddr = "10.0.0.1";
        String username = "joe";

        when(userService.userExistsByUsername(username)).thenReturn(true);
        when(userService.incrementNumberOfFailedLoginAttemptsAndLockAccount(username)).thenReturn(new User());

        //when
        service.loginFailed(remoteAddr, username);

        //then
        verify(userService, times(1)).userExistsByUsername(username);
        verify(loginAttemptRepository, times(1)).save(any());
        verify(userService, times(1)).incrementNumberOfFailedLoginAttemptsAndLockAccount(username);
    }

    @Test
    void testLoginFailed_userNotExists() {
        //given
        String remoteAddr = "10.0.0.1";
        String username = "joe";

        when(userService.userExistsByUsername(username)).thenReturn(false);
        when(userService.incrementNumberOfFailedLoginAttemptsAndLockAccount(username)).thenReturn(new User());

        //when
        service.loginFailed(remoteAddr, username);

        //then
        verify(userService, times(1)).userExistsByUsername(username);
        verify(loginAttemptRepository, times(0)).save(any());
        verify(userService, times(1)).incrementNumberOfFailedLoginAttemptsAndLockAccount(username);
    }

    @Test
    void testLoginSucceeded_userExists() {
        //given
        String remoteAddr = "10.0.0.1";
        String username = "joe";

        when(userService.userExistsByUsername(username)).thenReturn(true);

        //when
        service.loginSucceeded(remoteAddr, username);

        //then
        verify(userService, times(1)).userExistsByUsername(username);
        verify(loginAttemptRepository, times(1)).save(any());
        verify(userService, times(1)).resetNumberOfFailedLoginAttempts(username);
    }

    @Test
    void testLoginSucceeded_userNotExists() {
        //given
        String remoteAddr = "10.0.0.1";
        String username = "joe";

        when(userService.userExistsByUsername(username)).thenReturn(false);

        //when
        service.loginSucceeded(remoteAddr, username);

        //then
        verify(userService, times(1)).userExistsByUsername(username);
        verify(loginAttemptRepository, times(0)).save(any());
        verify(userService, times(1)).resetNumberOfFailedLoginAttempts(username);
    }


    @Test
    void testGetLastLoginAttemptsLogs() {
        //given
        String username = "joe";
        String failedIp = "1";
        String successIp = "2";
        LoginAttempt loginAttemptSuccessful = LoginAttempt.builder()
                .ip(successIp)
                .username(username)
                .result(ELoginAttemptResult.SUCCESSFUL)
                .build();
        LoginAttempt loginAttemptFailed = LoginAttempt.builder()
                .ip(failedIp)
                .username(username)
                .result(ELoginAttemptResult.FAILED)
                .build();
        LoginAttemptDTO loginAttemptDtoSuccessful = LoginAttemptDTO.builder()
                .ip(successIp)
                .username(username)
                .result(ELoginAttemptResult.SUCCESSFUL)
                .build();
        LoginAttemptDTO loginAttemptDtoFailed = LoginAttemptDTO.builder()
                .ip(failedIp)
                .username(username)
                .result(ELoginAttemptResult.FAILED)
                .build();

        when(loginAttemptRepository.getFirstByUsernameAndResultOrderByTimestampDesc(username, ELoginAttemptResult.FAILED))
                .thenReturn(loginAttemptFailed);
        when(loginAttemptRepository.getFirstByUsernameAndResultOrderByTimestampDesc(username, ELoginAttemptResult.SUCCESSFUL))
                .thenReturn(loginAttemptSuccessful);
        when(loginAttemptMapper.loginAttemptToDto(loginAttemptSuccessful))
                .thenReturn(loginAttemptDtoSuccessful);
        when(loginAttemptMapper.loginAttemptToDto(loginAttemptFailed))
                .thenReturn(loginAttemptDtoFailed);


        //when
        LastLoginAttemptsLogsResponse result = service.getLastLoginAttemptsLogs(username);

        //then
        verify(loginAttemptRepository, times(1))
                .getFirstByUsernameAndResultOrderByTimestampDesc(username, ELoginAttemptResult.FAILED);
        verify(loginAttemptRepository, times(1))
                .getFirstByUsernameAndResultOrderByTimestampDesc(username, ELoginAttemptResult.SUCCESSFUL);
        verify(loginAttemptMapper, times(1)).loginAttemptToDto(loginAttemptSuccessful);
        verify(loginAttemptMapper, times(1)).loginAttemptToDto(loginAttemptFailed);

        assertEquals(loginAttemptDtoFailed.getIp(), result.getLastFailedLoginAttempt().getIp());
        assertEquals(loginAttemptDtoSuccessful.getIp(), result.getLastSuccessfulLoginAttempt().getIp());
    }
}
