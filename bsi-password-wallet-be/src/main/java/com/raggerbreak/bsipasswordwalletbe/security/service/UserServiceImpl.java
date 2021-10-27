package com.raggerbreak.bsipasswordwalletbe.security.service;

import com.raggerbreak.bsipasswordwalletbe.security.model.User;
import com.raggerbreak.bsipasswordwalletbe.security.repository.UserRepository;
import com.raggerbreak.bsipasswordwalletbe.security.web.request.ChangeUserPasswordRequest;
import com.raggerbreak.bsipasswordwalletbe.security.web.response.MessageResponse;
import com.raggerbreak.bsipasswordwalletbe.wallet.PasswordUtils;
import com.raggerbreak.bsipasswordwalletbe.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private WalletService walletService;

    @Autowired
    public void setWalletService(WalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public MessageResponse changePassword(ChangeUserPasswordRequest request) throws Exception {
        User user = getCurrentAuthUser();

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
            return new MessageResponse("Invalid old password",true);

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
            String currentUsername = authentication.getName();
            return userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + currentUsername));
        } else {
            throw new AuthenticationServiceException("AnonymousAuthenticationToken");
        }
    }
}
