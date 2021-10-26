package com.raggerbreak.bsipasswordwalletbe.bootstrap;

import com.raggerbreak.bsipasswordwalletbe.security.model.ERole;
import com.raggerbreak.bsipasswordwalletbe.security.model.Role;
import com.raggerbreak.bsipasswordwalletbe.security.model.User;
import com.raggerbreak.bsipasswordwalletbe.security.repository.RoleRepository;
import com.raggerbreak.bsipasswordwalletbe.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        loadRoles();
//        loadUsers();
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

        userRepository.save(User.builder()
                .username("user")
                .email("user@email.com")
                .password(passwordEncoder.encode("123456"))
                .salt(BCrypt.gensalt())
                .roles(roles)
                .build());


        roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_ADMIN).orElse(null));
        userRepository.save(User.builder()
                .username("admin")
                .email("admin@email.com")
                .password(passwordEncoder.encode("123456"))
                .salt(BCrypt.gensalt())
                .roles(roles)
                .build());

        roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_USER).orElse(null));
        roles.add(roleRepository.findByName(ERole.ROLE_ADMIN).orElse(null));
        userRepository.save(User.builder()
                .username("all")
                .email("all@email.com")
                .password(passwordEncoder.encode("123456"))
                .salt(BCrypt.gensalt())
                .roles(roles)
                .build());
    }


}
