package com.raggerbreak.bsipasswordwalletbe.security.web.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponse {

    private String message;

    @JsonIgnore
    private boolean error;
}
