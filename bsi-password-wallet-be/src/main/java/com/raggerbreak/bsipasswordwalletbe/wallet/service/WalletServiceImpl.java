package com.raggerbreak.bsipasswordwalletbe.wallet.service;

import com.raggerbreak.bsipasswordwalletbe.security.model.User;
import com.raggerbreak.bsipasswordwalletbe.security.repository.UserRepository;
import com.raggerbreak.bsipasswordwalletbe.wallet.model.WalletPassword;
import com.raggerbreak.bsipasswordwalletbe.wallet.repository.WalletPasswordRepository;
import com.raggerbreak.bsipasswordwalletbe.wallet.dto.WalletPasswordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final CryptoService cryptoService;
    private final WalletPasswordRepository walletPasswordRepository;
    private final UserRepository userRepository;

    @Override
    public List<WalletPasswordDTO> getAllWalletPasswords(String username) {
        return walletPasswordRepository.findAllByUsername(username);
    }

    @Override
    public WalletPassword addPassword(WalletPasswordDTO dto) throws Exception {
        return walletPasswordRepository.save(WalletPassword.builder()
                .name(dto.getName())
                .login(dto.getLogin())
                .description(dto.getDescription())
                .password(cryptoService.encrypt(dto.getPassword(), getCurrentAuthUser()))
                .build());
    }

    private User getCurrentAuthUser() {
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
