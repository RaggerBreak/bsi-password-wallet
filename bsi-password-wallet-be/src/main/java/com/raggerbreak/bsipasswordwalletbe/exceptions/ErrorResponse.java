package com.raggerbreak.bsipasswordwalletbe.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ErrorResponse implements Serializable {
    private String message;
}
