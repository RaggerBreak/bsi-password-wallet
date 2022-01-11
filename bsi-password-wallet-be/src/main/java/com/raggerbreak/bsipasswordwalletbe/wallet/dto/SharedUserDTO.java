package com.raggerbreak.bsipasswordwalletbe.wallet.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedUserDTO implements Serializable {
    private static final long serialVersionUID = 2976201620007404118L;

    private Long id;
    private String email;
}
