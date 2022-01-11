package com.raggerbreak.bsipasswordwalletbe.exceptions;

public class WalletPasswordException extends RuntimeException {

    public WalletPasswordException() {
    }

    public WalletPasswordException(String message) {
        super(message);
    }
}
