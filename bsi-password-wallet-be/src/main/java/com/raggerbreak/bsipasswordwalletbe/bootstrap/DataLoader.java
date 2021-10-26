package com.raggerbreak.bsipasswordwalletbe.bootstrap;

import com.raggerbreak.bsipasswordwalletbe.security.model.ERole;
import com.raggerbreak.bsipasswordwalletbe.security.model.PasswordForm;
import com.raggerbreak.bsipasswordwalletbe.security.model.Role;
import com.raggerbreak.bsipasswordwalletbe.security.model.User;
import com.raggerbreak.bsipasswordwalletbe.security.repository.RoleRepository;
import com.raggerbreak.bsipasswordwalletbe.security.repository.UserRepository;
import com.raggerbreak.bsipasswordwalletbe.wallet.PasswordUtils;
import com.raggerbreak.bsipasswordwalletbe.wallet.model.WalletPassword;
import com.raggerbreak.bsipasswordwalletbe.wallet.repository.WalletPasswordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final WalletPasswordRepository walletPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        loadRoles();
        loadUsers();
    }

    public void loadRoles() {
        roleRepository.saveAll(Arrays.asList(
                Role.builder().name(ERole.ROLE_USER).build(),
                Role.builder().name(ERole.ROLE_ADMIN).build()
        ));
    }

    public void loadUsers() {

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_USER).orElse(null));

        User user = User.builder()
                .username("user")
                .email("user@email.com")
                .password(passwordEncoder.encode("123456"))
                .passwordForm(PasswordForm.SHA512)
                .salt(BCrypt.gensalt())
                .roles(roles)
                .build();
        user.setWalletPassword(PasswordUtils.encode(user));
        User savedUser = userRepository.save(user);

        Set<WalletPassword> walletPasswords = new HashSet<>();
        walletPasswords.add(WalletPassword.builder()
                .login("login1")
                .password("password1")
                        .user(savedUser)
                .build());
        walletPasswords.add(WalletPassword.builder()
                .login("login2")
                .password("password2")
                .user(savedUser)
                .build());
        walletPasswordRepository.saveAll(walletPasswords);

        roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_ADMIN).orElse(null));
        User admin = User.builder()
                .username("admin")
                .email("admin@email.com")
                .password(passwordEncoder.encode("123456"))
                .passwordForm(PasswordForm.SHA512)
                .salt(BCrypt.gensalt())
                .roles(roles)
                .build();
        admin.setWalletPassword(PasswordUtils.encode(admin));
        userRepository.save(admin);

        roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_USER).orElse(null));
        roles.add(roleRepository.findByName(ERole.ROLE_ADMIN).orElse(null));
        User all = User.builder()
                .username("all")
                .email("all@email.com")
                .password(passwordEncoder.encode("123456"))
                .passwordForm(PasswordForm.SHA512)
                .salt(BCrypt.gensalt())
                .roles(roles)
                .build();
        all.setWalletPassword(PasswordUtils.encode(all));
        userRepository.save(all);
    }


}
