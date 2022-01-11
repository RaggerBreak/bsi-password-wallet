package com.raggerbreak.bsipasswordwalletbe.wallet.service;

import com.raggerbreak.bsipasswordwalletbe.security.model.User;
import com.raggerbreak.bsipasswordwalletbe.security.service.UserService;
import com.raggerbreak.bsipasswordwalletbe.wallet.dto.WalletPasswordDTO;
import com.raggerbreak.bsipasswordwalletbe.wallet.mapper.WalletPasswordMapper;
import com.raggerbreak.bsipasswordwalletbe.wallet.model.WalletPassword;
import com.raggerbreak.bsipasswordwalletbe.wallet.repository.WalletPasswordRepository;
import com.raggerbreak.bsipasswordwalletbe.wallet.web.response.PasswordResponse;
import com.raggerbreak.bsipasswordwalletbe.wallet.web.response.SharePasswordResponse;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final CryptoService cryptoService;
    private final UserService userService;
    private final WalletPasswordRepository walletPasswordRepository;
    private final WalletPasswordMapper walletPasswordMapper;

    @Override
    public List<WalletPasswordDTO> getAllWalletPasswords(String username) {
        return walletPasswordRepository.findAllByUser_Username(username).stream()
                .map(walletPasswordMapper::walletPasswordToDTO)
                .collect(Collectors.toList());
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

    @Override
    public SharePasswordResponse sharePassword(Long passwordId, String userEmail) throws NotFoundException {
        WalletPassword walletPassword = getWalletPasswordByIdAndCurrentUserElseThrowNFE(passwordId);
        User shareUser = userService.getUserByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User To Share Not Found"));

        walletPassword.getSharedUsers().add(shareUser);
        WalletPassword savedPassword = walletPasswordRepository.save(walletPassword);

        return SharePasswordResponse.builder()
                .sharedPasswordId(savedPassword.getId())
                .build();
    }

    @Override
    public void deleteUserFromSharedPassword(Long passwordId, Long userId) throws NotFoundException {
        WalletPassword walletPassword = getWalletPasswordByIdAndCurrentUserElseThrowNFE(passwordId);
        User userToDelete = getUserByIdElseThrowNFE(userId);
        walletPassword.getSharedUsers().remove(userToDelete);
        walletPasswordRepository.save(walletPassword);
    }

    WalletPassword getWalletPasswordByIdAndCurrentUserElseThrowNFE(Long passwordId) throws NotFoundException {
        User currentUser = userService.getCurrentAuthUser();
        return walletPasswordRepository.findByIdAndUserId(passwordId, currentUser.getId())
                .orElseThrow(() -> new NotFoundException("Password Not Found"));
    }

    User getUserByIdElseThrowNFE(Long userId) throws NotFoundException {
        return userService.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User To Share Not Found"));
    }

}
