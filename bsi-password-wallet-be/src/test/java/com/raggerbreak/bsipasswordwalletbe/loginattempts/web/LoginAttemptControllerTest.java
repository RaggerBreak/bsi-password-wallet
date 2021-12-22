package com.raggerbreak.bsipasswordwalletbe.loginattempts.web;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.dto.IpAddressLockDTO;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.dto.LoginAttemptDTO;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.model.ELoginAttemptResult;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.service.IpAddressLockService;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.service.LoginAttemptService;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.web.response.LastLoginAttemptsLogsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginAttemptControllerTest {

    @Mock
    LoginAttemptService loginAttemptService;
    @Mock
    IpAddressLockService ipAddressLockService;

    @InjectMocks
    LoginAttemptController controller;

    Principal mockPrincipal;
    MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("name");
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();

    }

    @Test
    void testGetLastLoginAttemptsLogsResponse() throws Exception {
        LoginAttemptDTO lastFailedLoginAttempt = LoginAttemptDTO.builder()
                .ip("1")
                .result(ELoginAttemptResult.FAILED)
                .build();
        LoginAttemptDTO lastSuccessfulLoginAttempt = LoginAttemptDTO.builder()
                .ip("1")
                .result(ELoginAttemptResult.SUCCESSFUL)
                .build();

        when(loginAttemptService.getLastLoginAttemptsLogs(any())).thenReturn(LastLoginAttemptsLogsResponse.builder()
                .lastFailedLoginAttempt(lastFailedLoginAttempt)
                .lastSuccessfulLoginAttempt(lastSuccessfulLoginAttempt)
                .build());

        mockMvc.perform(get("/api/loginAttempt")
                        .principal(mockPrincipal)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPermanentlyLockedIpAddresses() throws Exception {
        List<IpAddressLockDTO> returnedIps = List.of(new IpAddressLockDTO(), new IpAddressLockDTO());

        when(ipAddressLockService.getPermanentlyLockedIpAddresses(any())).thenReturn(returnedIps);

        mockMvc.perform(get("/api/loginAttempt/permanentlyLockedIp")
                        .principal(mockPrincipal)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletePermanentlyLockedIpAddress() throws Exception {
        Long idToDelete = 1L;

        mockMvc.perform(delete("/api/loginAttempt/permanentlyLockedIp/" + idToDelete)
                        .principal(mockPrincipal)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
