package com.raggerbreak.bsipasswordwalletbe.loginattempts.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class IpAddressLock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ip;
    private String username;
    private Integer numberOfIncorrectAttempts;
    @Enumerated(EnumType.STRING)
    private EIpAddressLockStatus lockedStatus;
    private Date lockTime;
}
