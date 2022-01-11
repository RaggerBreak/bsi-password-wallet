package com.raggerbreak.bsipasswordwalletbe.wallet.model;

import com.raggerbreak.bsipasswordwalletbe.security.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class WalletPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String login;
    private String description;
    private String password;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "shared_user_to_shared_password",
            joinColumns = @JoinColumn(name = "shared_password_id"),
            inverseJoinColumns = @JoinColumn(name = "shared_user_id"))
    private Set<User> sharedUsers = new HashSet<>();


}
