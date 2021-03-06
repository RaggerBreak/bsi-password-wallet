package com.raggerbreak.bsipasswordwalletbe.wallet.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletPasswordDTO implements Serializable {
    private static final long serialVersionUID = -1757924360339459369L;

    private Long id;
    private String name;
    private String login;
    private String description;
    private String password;
    private Long ownerId;
    private List<SharedUserDTO> sharedUsers;
}
