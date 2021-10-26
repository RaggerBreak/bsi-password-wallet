package com.raggerbreak.bsipasswordwalletbe.wallet.web.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePasswordResponse implements Serializable {
    private static final long serialVersionUID = -1757924360339459369L;

    private Long walletPasswordId;
}
