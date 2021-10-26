package com.raggerbreak.bsipasswordwalletbe.wallet.repository;

import com.raggerbreak.bsipasswordwalletbe.wallet.model.WalletPassword;
import com.raggerbreak.bsipasswordwalletbe.wallet.dto.WalletPasswordDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WalletPasswordRepository extends JpaRepository<WalletPassword, Long> {

    @Query("select new com.raggerbreak.bsipasswordwalletbe.wallet.dto.WalletPasswordDTO(" +
            "wp.id, wp.name, wp.login, wp.description) " +
            "from WalletPassword wp " +
            "where wp.user.username = :username")
    List<WalletPasswordDTO> findAllByUsername(String username);
}
