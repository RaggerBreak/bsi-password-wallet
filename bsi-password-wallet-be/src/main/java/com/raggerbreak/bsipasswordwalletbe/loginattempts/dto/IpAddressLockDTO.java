package com.raggerbreak.bsipasswordwalletbe.loginattempts.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IpAddressLockDTO implements Serializable {
    private static final long serialVersionUID = -4968103648512574785L;

    private Long id;
    private String ip;
}
