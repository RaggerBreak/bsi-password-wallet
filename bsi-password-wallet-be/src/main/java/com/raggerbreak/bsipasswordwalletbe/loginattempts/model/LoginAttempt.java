package com.raggerbreak.bsipasswordwalletbe.loginattempts.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ip;

    @Enumerated(EnumType.STRING)
    private ELoginAttemptResult result;

    @CreationTimestamp
    private Date timestamp;

    private String username;
}
