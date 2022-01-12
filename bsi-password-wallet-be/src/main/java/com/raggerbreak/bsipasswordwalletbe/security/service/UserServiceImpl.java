package com.raggerbreak.bsipasswordwalletbe.security.service;

import com.raggerbreak.bsipasswordwalletbe.security.model.PasswordAccessMode;
import com.raggerbreak.bsipasswordwalletbe.security.model.User;
import com.raggerbreak.bsipasswordwalletbe.security.repository.UserRepository;
import com.raggerbreak.bsipasswordwalletbe.security.web.request.ChangeUserPasswordRequest;
import com.raggerbreak.bsipasswordwalletbe.security.web.response.MessageResponse;
import com.raggerbreak.bsipasswordwalletbe.wallet.PasswordUtils;
import com.raggerbreak.bsipasswordwalletbe.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private WalletService walletService;

    private final Integer MAX_FAILED_ATTEMPTS = 4;
    private final Integer FIVE_SECONDS_IN_MILLIS = 5000;
    private final Integer TEN_SECONDS_IN_MILLIS = 10000;
    private final Integer TWO_MIN_IN_MILLIS = 120000;

    @Autowired
    public void setWalletService(WalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public MessageResponse changePassword(ChangeUserPasswordRequest request) throws Exception {
        User user = getCurrentAuthUser();

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
            return new MessageResponse("Invalid old password", true);

        String oldWalletPassword = user.getWalletPassword();


        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordForm(request.getPasswordForm());
        user.setSalt(BCrypt.gensalt());
        user.setWalletPassword(PasswordUtils.encode(user));
        User savedUsed = userRepository.save(user);

        walletService.onChangeUserPassword(oldWalletPassword, savedUsed);

        return new MessageResponse("Password was changed successfully", false);
    }

    @Override
    public User getCurrentAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return getUserByUsername(authentication.getName());
        } else {
            throw new AuthenticationServiceException("AnonymousAuthenticationToken");
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }


    @Override
    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public User incrementNumberOfFailedLoginAttemptsAndLockAccount(String username) {
        log.debug("UserServiceImpl: incrementNumberOfFailedLoginAttemptsAndLockAccount");
        User user = getUserByUsername(username);

        Integer numberOfFailedLoginAttempts = Optional.ofNullable(user.getNumberOfFailedLoginAttempts()).orElse(0);
        numberOfFailedLoginAttempts++;
        user.setNumberOfFailedLoginAttempts(numberOfFailedLoginAttempts);

        switch (numberOfFailedLoginAttempts) {
            case 2:
                user.setLocked(true);
                user.setLockTime(new Date(new Date().getTime() + FIVE_SECONDS_IN_MILLIS));
                break;
            case 3:
                user.setLocked(true);
                user.setLockTime(new Date(new Date().getTime() + TEN_SECONDS_IN_MILLIS));
                break;
            case 4:
                user.setLocked(true);
                user.setLockTime(new Date(new Date().getTime() + TWO_MIN_IN_MILLIS));
                break;
        }
        return userRepository.save(user);
    }

    @Override
    public void resetNumberOfFailedLoginAttempts(String username) {
        log.debug("UserServiceImpl: resetNumberOfFailedLoginAttemptsAndUnlockAccount");
        User user = getUserByUsername(username);
        user.setNumberOfFailedLoginAttempts(0);
        userRepository.save(user);
    }

    @Override
    public boolean checkIfLockTimeExpiredThenUnlockAccount(String username) {
        log.debug("UserServiceImpl: checkIfLockTimeExpiredThenUnlockAccount");
        User user = getUserByUsername(username);
        if (Objects.nonNull(user.getLockTime()) && user.getLockTime().getTime() < new Date().getTime()) {
            user.setLocked(false);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public PasswordAccessMode changeAccessMode(PasswordAccessMode accessMode) {
        User user = getCurrentAuthUser();
        user.setPasswordAccessMode(accessMode);
        return userRepository.save(user).getPasswordAccessMode();
    }

    @Override
    public PasswordAccessMode getAccessMode() {
        return getCurrentAuthUser().getPasswordAccessMode();
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    }

}
