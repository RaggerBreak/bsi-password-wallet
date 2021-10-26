package com.raggerbreak.bsipasswordwalletbe.wallet.service;

import com.raggerbreak.bsipasswordwalletbe.wallet.dto.WalletPasswordDTO;

import java.util.List;

public interface WalletService {
    List<WalletPasswordDTO> getAllWalletPasswords(String username);
}
