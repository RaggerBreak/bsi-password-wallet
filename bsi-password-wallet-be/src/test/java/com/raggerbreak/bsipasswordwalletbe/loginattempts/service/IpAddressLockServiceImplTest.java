package com.raggerbreak.bsipasswordwalletbe.loginattempts.service;

import com.raggerbreak.bsipasswordwalletbe.exceptions.IpLockedException;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.dto.IpAddressLockDTO;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.model.IpAddressLock;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.repository.IpAddressLockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;

import static com.raggerbreak.bsipasswordwalletbe.loginattempts.model.EIpAddressLockStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IpAddressLockServiceImplTest {

    @Mock
    IpAddressLockRepository ipAddressLockRepository;

    IpAddressLockServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new IpAddressLockServiceImpl(ipAddressLockRepository);
    }

    @Test
    void testLoginFailed_oneWrongAttempts_notLocked() {
        //given
        String remoteAddr = "10.0.0.1";
        String username = "joe";
        IpAddressLock createdIpAddressLock = IpAddressLock.builder()
                .ip(remoteAddr)
                .username(username)
                .numberOfIncorrectAttempts(0)
                .lockedStatus(OPEN)
                .build();

        when(ipAddressLockRepository.findByIpAndUsername(remoteAddr, username))
                .thenReturn(java.util.Optional.ofNullable(createdIpAddressLock));

        //when
        service.loginFailed(remoteAddr, username);

        //then
        verify(ipAddressLockRepository, times(1)).findByIpAndUsername(remoteAddr, username);
        verify(ipAddressLockRepository, times(1)).save(createdIpAddressLock);

        assertEquals(createdIpAddressLock.getNumberOfIncorrectAttempts(), 1);
        assertEquals(createdIpAddressLock.getLockedStatus(), OPEN);
    }

    @Test
    void testLoginFailed_twoWrongAttempts_locked() {
        //given
        String remoteAddr = "10.0.0.1";
        String username = "joe";
        IpAddressLock createdIpAddressLock = IpAddressLock.builder()
                .ip(remoteAddr)
                .username(username)
                .numberOfIncorrectAttempts(1)
                .lockedStatus(OPEN)
                .build();

        when(ipAddressLockRepository.findByIpAndUsername(remoteAddr, username))
                .thenReturn(java.util.Optional.ofNullable(createdIpAddressLock));

        //when
        service.loginFailed(remoteAddr, username);

        //then
        verify(ipAddressLockRepository, times(1)).findByIpAndUsername(remoteAddr, username);
        verify(ipAddressLockRepository, times(1)).save(createdIpAddressLock);

        assertEquals(createdIpAddressLock.getNumberOfIncorrectAttempts(), 2);
        assertEquals(createdIpAddressLock.getLockedStatus(), LOCKED);
    }

    @Test
    void testLoginFailed_threeWrongAttempts_locked() {
        //given
        String remoteAddr = "10.0.0.1";
        String username = "joe";
        IpAddressLock createdIpAddressLock = IpAddressLock.builder()
                .ip(remoteAddr)
                .username(username)
                .numberOfIncorrectAttempts(2)
                .lockedStatus(OPEN)
                .build();

        when(ipAddressLockRepository.findByIpAndUsername(remoteAddr, username))
                .thenReturn(java.util.Optional.ofNullable(createdIpAddressLock));

        //when
        service.loginFailed(remoteAddr, username);

        //then
        verify(ipAddressLockRepository, times(1)).findByIpAndUsername(remoteAddr, username);
        verify(ipAddressLockRepository, times(1)).save(createdIpAddressLock);

        assertEquals(createdIpAddressLock.getNumberOfIncorrectAttempts(), 3);
        assertEquals(createdIpAddressLock.getLockedStatus(), LOCKED);
    }

    @Test
    void testLoginFailed_fourWrongAttempts_permanentlyLocked() {
        //given
        String remoteAddr = "10.0.0.1";
        String username = "joe";
        IpAddressLock createdIpAddressLock = IpAddressLock.builder()
                .ip(remoteAddr)
                .username(username)
                .numberOfIncorrectAttempts(3)
                .lockedStatus(LOCKED)
                .build();

        when(ipAddressLockRepository.findByIpAndUsername(remoteAddr, username))
                .thenReturn(java.util.Optional.ofNullable(createdIpAddressLock));

        //when
        service.loginFailed(remoteAddr, username);

        //then
        verify(ipAddressLockRepository, times(1)).findByIpAndUsername(remoteAddr, username);
        verify(ipAddressLockRepository, times(1)).save(createdIpAddressLock);

        assertEquals(createdIpAddressLock.getNumberOfIncorrectAttempts(), 4);
        assertEquals(createdIpAddressLock.getLockedStatus(), PERMANENTLY_LOCKED);
    }

    @Test
    void testLoginSucceeded_addressLockExists_openLockedStatus() {
        //given
        String remoteAddr = "10.0.0.1";
        String username = "joe";
        IpAddressLock createdIpAddressLock = IpAddressLock.builder()
                .ip(remoteAddr)
                .username(username)
                .numberOfIncorrectAttempts(3)
                .lockedStatus(LOCKED)
                .build();

        when(ipAddressLockRepository.findByIpAndUsername(remoteAddr, username))
                .thenReturn(java.util.Optional.ofNullable(createdIpAddressLock));

        //when
        service.loginSucceeded(remoteAddr, username);

        //then
        verify(ipAddressLockRepository, times(1)).findByIpAndUsername(remoteAddr, username);
        verify(ipAddressLockRepository, times(1)).save(createdIpAddressLock);

        assertEquals(createdIpAddressLock.getNumberOfIncorrectAttempts(), 0);
        assertEquals(createdIpAddressLock.getLockedStatus(), OPEN);
    }

    @Test
    void testLoginSucceeded_addressLockNotExists_doNotChange() {
        //given
        String remoteAddr = "10.0.0.1";
        String username = "joe";
        IpAddressLock createdIpAddressLock = IpAddressLock.builder()
                .ip(remoteAddr)
                .username(username)
                .numberOfIncorrectAttempts(4)
                .lockedStatus(PERMANENTLY_LOCKED)
                .build();

        when(ipAddressLockRepository.findByIpAndUsername(remoteAddr, username))
                .thenReturn(java.util.Optional.ofNullable(createdIpAddressLock));

        //when
        service.loginSucceeded(remoteAddr, username);

        //then
        verify(ipAddressLockRepository, times(1)).findByIpAndUsername(remoteAddr, username);
        verify(ipAddressLockRepository, times(0)).save(createdIpAddressLock);
    }

    @Test
    void testLoginSucceeded_permanentlyLocked_doNotChange() {
        //given
        String remoteAddr = "10.0.0.1";
        String username = "joe";
        IpAddressLock createdIpAddressLock = IpAddressLock.builder()
                .ip(remoteAddr)
                .username(username)
                .numberOfIncorrectAttempts(4)
                .lockedStatus(PERMANENTLY_LOCKED)
                .build();

        when(ipAddressLockRepository.findByIpAndUsername(remoteAddr, username))
                .thenReturn(java.util.Optional.ofNullable(createdIpAddressLock));

        //when
        service.loginSucceeded(remoteAddr, username);

        //then
        verify(ipAddressLockRepository, times(1)).findByIpAndUsername(remoteAddr, username);
        verify(ipAddressLockRepository, times(0)).save(createdIpAddressLock);
    }

    @Test
    void testCheckIfLockedDependsOnStatus_open() {
        //given
        String remoteAddr = "10.0.0.1";
        String username = "joe";
        IpAddressLock ipAddressLock = IpAddressLock.builder()
                .ip(remoteAddr)
                .username(username)
                .numberOfIncorrectAttempts(0)
                .lockedStatus(OPEN)
                .build();

        when(ipAddressLockRepository.findByIpAndUsername(remoteAddr, username))
                .thenReturn(java.util.Optional.ofNullable(ipAddressLock));

        //when
        boolean result = service.checkIfLockedDependsOnStatus(remoteAddr, username);

        //then
        verify(ipAddressLockRepository, times(1)).findByIpAndUsername(remoteAddr, username);
        verify(ipAddressLockRepository, times(0)).save(any());

        assertEquals(false, result);
    }

    @Test
    void testCheckIfLockedDependsOnStatus_locked() {
        //given
        String remoteAddr = "10.0.0.1";
        String username = "joe";
        String errorMessage = "IP is locked";
        IpAddressLock ipAddressLock = IpAddressLock.builder()
                .ip(remoteAddr)
                .lockTime(new Date(new Date().getTime() - 10000))
                .username(username)
                .numberOfIncorrectAttempts(3)
                .lockedStatus(LOCKED)
                .build();

        when(ipAddressLockRepository.findByIpAndUsername(remoteAddr, username))
                .thenReturn(java.util.Optional.ofNullable(ipAddressLock));

        //when
        IpLockedException thrown = assertThrows(
                IpLockedException.class,
                () -> service.checkIfLockedDependsOnStatus(remoteAddr, username),
                errorMessage
        );

        //then
        verify(ipAddressLockRepository, times(1)).findByIpAndUsername(remoteAddr, username);
        verify(ipAddressLockRepository, times(1)).save(any());

        assertTrue(thrown.getMessage().contains(errorMessage));
    }

    @Test
    void testCheckIfLockedDependsOnStatus_permanentlyLocked() {
        //given
        String remoteAddr = "10.0.0.1";
        String username = "joe";
        String errorMessage = "IP is permanently locked";
        IpAddressLock ipAddressLock = IpAddressLock.builder()
                .ip(remoteAddr)
                .lockTime(new Date())
                .username(username)
                .numberOfIncorrectAttempts(4)
                .lockedStatus(PERMANENTLY_LOCKED)
                .build();

        when(ipAddressLockRepository.findByIpAndUsername(remoteAddr, username))
                .thenReturn(java.util.Optional.ofNullable(ipAddressLock));

        //when
        IpLockedException thrown = assertThrows(
                IpLockedException.class,
                () -> service.checkIfLockedDependsOnStatus(remoteAddr, username),
                errorMessage
        );

        //then
        verify(ipAddressLockRepository, times(1)).findByIpAndUsername(remoteAddr, username);
        verify(ipAddressLockRepository, times(0)).save(any());

        assertTrue(thrown.getMessage().contains(errorMessage));
    }

    @Test
    void testGetPermanentlyLockedIpAddresses() {
        //given
        List<IpAddressLockDTO> givenList = List.of(new IpAddressLockDTO(), new IpAddressLockDTO());
        when(ipAddressLockRepository.findAllPermanentlyLockedIpAddressesByUsername(any())).thenReturn(givenList);

        //when
        List<IpAddressLockDTO> result = service.getPermanentlyLockedIpAddresses(any());

        //then
        verify(ipAddressLockRepository, times(1)).findAllPermanentlyLockedIpAddressesByUsername(any());
        assertEquals(result.size(), givenList.size());
    }

    @Test
    void testDeletePermanentlyLockedIpAddress_addressLockExists() {
        //given
        Long ipAddressId = 1L;
        String username = "joe";
        IpAddressLock createdIpAddressLock = IpAddressLock.builder()
                .username(username)
                .numberOfIncorrectAttempts(4)
                .lockedStatus(PERMANENTLY_LOCKED)
                .build();

        when(ipAddressLockRepository.findByIdAndUsername(ipAddressId, username))
                .thenReturn(java.util.Optional.ofNullable(createdIpAddressLock));

        //when
        service.deletePermanentlyLockedIpAddress(ipAddressId, username);

        //then
        verify(ipAddressLockRepository, times(1)).delete(createdIpAddressLock);
    }

    @Test
    void testDeletePermanentlyLockedIpAddress_addressLockNotExists() {
        //given
        Long ipAddressId = 1L;
        String username = "joe";

        when(ipAddressLockRepository.findByIdAndUsername(ipAddressId, username))
                .thenReturn(java.util.Optional.empty());

        //when
        service.deletePermanentlyLockedIpAddress(ipAddressId, username);

        //then
        verify(ipAddressLockRepository, times(0)).delete(any());
    }
}
