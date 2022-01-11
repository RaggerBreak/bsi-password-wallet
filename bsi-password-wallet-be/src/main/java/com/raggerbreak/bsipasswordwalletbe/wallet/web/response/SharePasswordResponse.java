package com.raggerbreak.bsipasswordwalletbe.wallet.web.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharePasswordResponse implements Serializable {
    private static final long serialVersionUID = 1917238184135449683L;

    private Long sharedPasswordId;
}
