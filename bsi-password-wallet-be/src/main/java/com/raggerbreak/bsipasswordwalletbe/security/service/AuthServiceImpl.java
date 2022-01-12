package com.raggerbreak.bsipasswordwalletbe.security.service;

import com.raggerbreak.bsipasswordwalletbe.loginattempts.service.IpAddressLockService;
import com.raggerbreak.bsipasswordwalletbe.security.jwt.JwtUtils;
import com.raggerbreak.bsipasswordwalletbe.security.model.ERole;
import com.raggerbreak.bsipasswordwalletbe.security.model.PasswordAccessMode;
import com.raggerbreak.bsipasswordwalletbe.security.model.Role;
import com.raggerbreak.bsipasswordwalletbe.security.model.User;
import com.raggerbreak.bsipasswordwalletbe.security.repository.RoleRepository;
import com.raggerbreak.bsipasswordwalletbe.security.repository.UserRepository;
import com.raggerbreak.bsipasswordwalletbe.security.web.request.LoginRequest;
import com.raggerbreak.bsipasswordwalletbe.security.web.request.SignupRequest;
import com.raggerbreak.bsipasswordwalletbe.security.web.response.JwtResponse;
import com.raggerbreak.bsipasswordwalletbe.security.web.response.MessageResponse;
import com.raggerbreak.bsipasswordwalletbe.wallet.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final HttpServletRequest request;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final IpAddressLockService ipAddressLockService;

    private final JwtUtils jwtUtils;

    @Override
    public JwtResponse signin(LoginRequest loginRequest) {
        userService.checkIfLockTimeExpiredThenUnlockAccount(loginRequest.getUsername());
        ipAddressLockService.checkIfLockedDependsOnStatus(request.getRemoteAddr(), loginRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwt(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        userService.changeAccessMode(PasswordAccessMode.READ);

        return JwtResponse.builder()
                .token(jwt)
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
    }

    @Override
    public MessageResponse signup(SignupRequest signupRequest) {

        if (userService.userExistsByUsername(signupRequest.getUsername())) {
            return new MessageResponse("Error: Username is already in use!", true);
        }

        if (userService.userExistsByUsername(signupRequest.getEmail())) {
            return new MessageResponse("Error: Email is already in use!", true);
        }

        createUser(signupRequest);

        return new MessageResponse("User registered successfully!", false);
    }

    private User createUser(SignupRequest signupRequest) {

        User user = User.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .passwordForm(signupRequest.getPasswordForm())
                .salt(BCrypt.gensalt())
                .numberOfFailedLoginAttempts(0)
                .locked(false)
                .passwordAccessMode(PasswordAccessMode.READ)
                .build();

        user.setWalletPassword(PasswordUtils.encode(user));

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (Objects.isNull(strRoles)) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if ("admin".equals(role)) {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        return userRepository.save(user);
    }
}
