package com.raggerbreak.bsipasswordwalletbe.wallet.repository;

import com.raggerbreak.bsipasswordwalletbe.wallet.model.WalletPassword;
import com.raggerbreak.bsipasswordwalletbe.wallet.dto.WalletPasswordDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WalletPasswordRepository extends JpaRepository<WalletPassword, Long> {

    @Query("select new com.raggerbreak.bsipasswordwalletbe.wallet.dto.WalletPasswordDTO(" +
            "wp.id, wp.name, wp.login, wp.description, wp.password) " +
            "from WalletPassword wp " +
            "where wp.user.username = :username")
    List<WalletPasswordDTO> findAllByUsername(String username);

    List<WalletPassword> findAllByUserId(Long id);

    Optional<WalletPassword> findByIdAndUserId(Long passwordId, Long userId);
}
