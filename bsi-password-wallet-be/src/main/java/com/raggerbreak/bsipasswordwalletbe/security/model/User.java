package com.raggerbreak.bsipasswordwalletbe.security.model;

import com.raggerbreak.bsipasswordwalletbe.wallet.model.WalletPassword;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String walletPassword;

    private String salt;

    @Enumerated(EnumType.STRING)
    private PasswordForm passwordForm;

    private Integer numberOfFailedLoginAttempts;

    private boolean locked;

    private Date lockTime;

    @Enumerated(EnumType.STRING)
    private PasswordAccessMode passwordAccessMode;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "sharedUsers")
    Set<WalletPassword> sharedPasswords;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<WalletPassword> walletPasswords = new HashSet<>();
}
