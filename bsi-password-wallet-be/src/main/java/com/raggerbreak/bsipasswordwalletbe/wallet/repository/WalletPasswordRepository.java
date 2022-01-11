package com.raggerbreak.bsipasswordwalletbe.wallet.repository;

import com.raggerbreak.bsipasswordwalletbe.security.model.User;
import com.raggerbreak.bsipasswordwalletbe.wallet.model.WalletPassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletPasswordRepository extends JpaRepository<WalletPassword, Long> {

    List<WalletPassword> findAllByUser_Username(String username);
    List<WalletPassword> findAllByUserId(Long id);
    Optional<WalletPassword> findByIdAndUserId(Long passwordId, Long userId);
    List<WalletPassword> findAllBySharedUsers(User user);
}
