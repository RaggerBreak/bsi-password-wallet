package com.raggerbreak.bsipasswordwalletbe.wallet.service;

import com.raggerbreak.bsipasswordwalletbe.security.model.User;

public interface CryptoService {

    String encrypt(String password, User user) throws Exception;
    String decrypt(String password, User user) throws Exception;
}
