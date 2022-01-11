package com.raggerbreak.bsipasswordwalletbe.exceptions.controller;

import com.raggerbreak.bsipasswordwalletbe.exceptions.ErrorResponse;
import com.raggerbreak.bsipasswordwalletbe.exceptions.IpLockedException;
import com.raggerbreak.bsipasswordwalletbe.exceptions.WalletPasswordException;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(AuthenticationException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IpLockedException.class)
    public ResponseEntity<ErrorResponse> handleIpLockedException(IpLockedException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(WalletPasswordException.class)
    public ResponseEntity<ErrorResponse> handleWalletPasswordException(WalletPasswordException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public static ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }
}
