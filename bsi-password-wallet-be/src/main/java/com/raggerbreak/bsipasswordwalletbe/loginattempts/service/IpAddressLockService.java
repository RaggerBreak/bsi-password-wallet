package com.raggerbreak.bsipasswordwalletbe.loginattempts.service;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.dto.IpAddressLockDTO;

import java.util.List;

public interface IpAddressLockService {
    void loginFailed(String remoteAddr, String username);
    void loginSucceeded(String remoteAddr, String username);
    boolean checkIfLockedDependsOnStatus(String remoteAddr, String username);
    List<IpAddressLockDTO> getPermanentlyLockedIpAddresses(String username);
    void deletePermanentlyLockedIpAddress(Long ipAddressId, String username);
}
