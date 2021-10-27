package com.raggerbreak.bsipasswordwalletbe.wallet.service;

import com.raggerbreak.bsipasswordwalletbe.security.model.User;
import com.raggerbreak.bsipasswordwalletbe.security.service.UserService;
import com.raggerbreak.bsipasswordwalletbe.wallet.model.WalletPassword;
import com.raggerbreak.bsipasswordwalletbe.wallet.repository.WalletPasswordRepository;
import com.raggerbreak.bsipasswordwalletbe.wallet.dto.WalletPasswordDTO;
import com.raggerbreak.bsipasswordwalletbe.wallet.web.response.PasswordResponse;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final CryptoService cryptoService;
    private final UserService userService;
    private final WalletPasswordRepository walletPasswordRepository;

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
                .password(cryptoService.encrypt(dto.getPassword(), userService.getCurrentAuthUser().getWalletPassword()))
                .user(userService.getCurrentAuthUser())
                .build());
    }

    @Override
    public PasswordResponse decodePassword(Long passwordId) throws Exception {
        User user = userService.getCurrentAuthUser();
        WalletPassword walletPassword = walletPasswordRepository.findByIdAndUserId(passwordId, user.getId())
                .orElseThrow(() -> new NotFoundException("Password Not Found"));

        return PasswordResponse.builder()
                .password(cryptoService.decrypt(walletPassword.getPassword(), user.getWalletPassword()))
                .build();
    }

    @Override
    public void deletePassword(Long passwordId) throws Exception {
        User user = userService.getCurrentAuthUser();
        WalletPassword walletPassword = walletPasswordRepository.findByIdAndUserId(passwordId, user.getId())
                .orElseThrow(() -> new NotFoundException("Password Not Found"));

        walletPasswordRepository.deleteById(passwordId);
    }

    @Override
    public void onChangeUserPassword(String oldWalletPassword, User user) throws Exception {

        List<WalletPassword> walletPasswords = walletPasswordRepository.findAllByUserId(user.getId());

        for (WalletPassword passwd : walletPasswords) {
            String plainPassword = cryptoService.decrypt(passwd.getPassword(), oldWalletPassword);
            passwd.setPassword(cryptoService.encrypt(plainPassword, user.getWalletPassword()));
            walletPasswordRepository.save(passwd);
        }
    }

}
