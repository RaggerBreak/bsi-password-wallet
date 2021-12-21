package com.raggerbreak.bsipasswordwalletbe.loginattempts.repository;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.dto.IpAddressLockDTO;
import com.raggerbreak.bsipasswordwalletbe.loginattempts.model.IpAddressLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IpAddressLockRepository extends JpaRepository<IpAddressLock, Long> {
    Optional<IpAddressLock> findByIpAndUsername(String ip, String username);
    Optional<IpAddressLock> findByIdAndUsername(Long id, String username);

    @Query("select new com.raggerbreak.bsipasswordwalletbe.loginattempts.dto.IpAddressLockDTO(" +
            "ipAddr.id, ipAddr.ip) " +
            "from IpAddressLock ipAddr where ipAddr.username = :username and ipAddr.lockedStatus  = 'PERMANENTLY_LOCKED'")
    List<IpAddressLockDTO> findAllPermanentlyLockedIpAddressesByUsername(String username);
}
