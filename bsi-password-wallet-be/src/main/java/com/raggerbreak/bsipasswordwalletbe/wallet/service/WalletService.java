package com.raggerbreak.bsipasswordwalletbe.wallet.service;

import com.raggerbreak.bsipasswordwalletbe.security.model.User;
import com.raggerbreak.bsipasswordwalletbe.wallet.dto.WalletPasswordDTO;
import com.raggerbreak.bsipasswordwalletbe.wallet.model.WalletPassword;
import com.raggerbreak.bsipasswordwalletbe.wallet.web.response.PasswordResponse;

import java.util.List;

public interface WalletService {

    List<WalletPasswordDTO> getAllWalletPasswords(String username);
    WalletPassword addPassword(WalletPasswordDTO walletPasswordDTO) throws Exception ;
    PasswordResponse decodePassword(Long passwordId) throws Exception;
    void deletePassword(Long passwordId) throws Exception;
    void onChangeUserPassword(String oldWalletPassword, User newUser) throws Exception;
}
