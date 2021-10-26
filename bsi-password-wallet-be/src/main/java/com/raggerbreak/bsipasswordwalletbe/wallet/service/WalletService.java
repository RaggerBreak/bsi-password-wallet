package com.raggerbreak.bsipasswordwalletbe.wallet.service;

import com.raggerbreak.bsipasswordwalletbe.wallet.dto.WalletPasswordDTO;
import com.raggerbreak.bsipasswordwalletbe.wallet.model.WalletPassword;

import java.util.List;

public interface WalletService {
    List<WalletPasswordDTO> getAllWalletPasswords(String username);

    WalletPassword addPassword(WalletPasswordDTO walletPasswordDTO) throws Exception ;
}
