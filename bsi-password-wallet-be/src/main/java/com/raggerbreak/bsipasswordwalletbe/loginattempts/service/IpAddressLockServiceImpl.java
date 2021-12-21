package com.raggerbreak.bsipasswordwalletbe.loginattempts.service;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.dto.IpAddressLockDTO;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.model.IpAddressLock;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.repository.IpAddressLockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.raggerbreak.bsipasswordwalletbe.loginattempts.model.EIpAddressLockStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class IpAddressLockServiceImpl implements IpAddressLockService {

    private final IpAddressLockRepository ipAddressLockRepository;

    private final Integer MAX_FAILED_ATTEMPTS = 4;
    private final Integer FIVE_SECONDS_IN_MILLIS = 5000;
    private final Integer TEN_SECONDS_IN_MILLIS = 10000;

    @Override
    public void loginFailed(String remoteAddr, String username) {
        log.debug("IpAddressLockServiceImpl: loginFailed");
        IpAddressLock ipAddressLock = ipAddressLockRepository.findByIpAndUsername(remoteAddr, username)
                .orElse(IpAddressLock.builder()
                        .ip(remoteAddr)
                        .username(username)
                        .numberOfIncorrectAttempts(0)
                        .lockedStatus(OPEN)
                        .build());
        Integer numberOfIncorrectAttempts = Optional.ofNullable(ipAddressLock.getNumberOfIncorrectAttempts()).orElse(0);
        if (numberOfIncorrectAttempts < MAX_FAILED_ATTEMPTS) {
            numberOfIncorrectAttempts++;
        }
        ipAddressLock.setNumberOfIncorrectAttempts(numberOfIncorrectAttempts);

        switch (numberOfIncorrectAttempts) {
            case 2:
                ipAddressLock.setLockedStatus(LOCKED);
                ipAddressLock.setLockTime(new Date(new Date().getTime() + FIVE_SECONDS_IN_MILLIS));
                break;
            case 3:
                ipAddressLock.setLockedStatus(LOCKED);
                ipAddressLock.setLockTime(new Date(new Date().getTime() + TEN_SECONDS_IN_MILLIS));
                break;
            case 4:
                ipAddressLock.setLockedStatus(PERMANENTLY_LOCKED);
                break;
        }
        ipAddressLockRepository.save(ipAddressLock);
    }

    @Override
    public void loginSucceeded(String remoteAddr, String username) {
        log.debug("IpAddressLockServiceImpl: loginSucceeded");
        IpAddressLock ipAddressLock = ipAddressLockRepository.findByIpAndUsername(remoteAddr, username).orElse(null);
        if (Objects.nonNull(ipAddressLock) && !PERMANENTLY_LOCKED.equals(ipAddressLock.getLockedStatus())) {
            ipAddressLock.setNumberOfIncorrectAttempts(0);
            ipAddressLock.setLockedStatus(OPEN);
            ipAddressLockRepository.save(ipAddressLock);
        }
    }

    @Override
    public boolean checkIfLockedDependsOnStatus(String remoteAddr, String username) {
        log.debug("IpAddressLockServiceImpl: checkIfLockTimeExpiredThenUnlockAccount");
        IpAddressLock ipAddressLock = ipAddressLockRepository.findByIpAndUsername(remoteAddr, username).orElse(null);
        if (Objects.nonNull(ipAddressLock) && Objects.nonNull(ipAddressLock.getLockedStatus())) {
            switch (ipAddressLock.getLockedStatus()) {
                case PERMANENTLY_LOCKED:
                    throw new AuthenticationServiceException("IP PERMANENTLY_LOCKED");
                case OPEN:
                    return true;
                case LOCKED:
                    if (Objects.nonNull(ipAddressLock.getLockTime()) && ipAddressLock.getLockTime().getTime() < new Date().getTime()) {
                        ipAddressLock.setLockedStatus(OPEN);
                        ipAddressLockRepository.save(ipAddressLock);
                        return true;
                    } else {
                        throw new AuthenticationServiceException("IP LOCKED");
                    }
            }
        }
        return true;
    }

    @Override
    public List<IpAddressLockDTO> getPermanentlyLockedIpAddresses(String username) {
        return ipAddressLockRepository.findAllPermanentlyLockedIpAddressesByUsername(username);
    }

    @Override
    public void deletePermanentlyLockedIpAddress(Long ipAddressId, String username) {
        IpAddressLock ipAddressLock = ipAddressLockRepository.findByIdAndUsername(ipAddressId, username).orElse(null);
        if (Objects.nonNull(ipAddressLock)) {
            ipAddressLockRepository.delete(ipAddressLock);
        }
    }
}
