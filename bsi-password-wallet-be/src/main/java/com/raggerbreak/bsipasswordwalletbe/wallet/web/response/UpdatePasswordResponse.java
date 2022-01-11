package com.raggerbreak.bsipasswordwalletbe.wallet.web.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePasswordResponse implements Serializable {
    private static final long serialVersionUID = 2923774323684370494L;

    private Long walletPasswordId;
}
