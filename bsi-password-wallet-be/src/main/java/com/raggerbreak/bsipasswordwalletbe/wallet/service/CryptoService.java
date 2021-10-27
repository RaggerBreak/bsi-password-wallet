package com.raggerbreak.bsipasswordwalletbe.wallet.service;


public interface CryptoService {

    String encrypt(String password, String walletPassword) throws Exception;
    String decrypt(String password, String walletPassword) throws Exception;
}
