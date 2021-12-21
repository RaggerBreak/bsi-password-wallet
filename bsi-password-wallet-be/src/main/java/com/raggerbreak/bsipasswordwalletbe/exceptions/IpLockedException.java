package com.raggerbreak.bsipasswordwalletbe.exceptions;

public class IpLockedException extends RuntimeException {

    public IpLockedException() {
    }

    public IpLockedException(String message) {
        super(message);
    }
}
