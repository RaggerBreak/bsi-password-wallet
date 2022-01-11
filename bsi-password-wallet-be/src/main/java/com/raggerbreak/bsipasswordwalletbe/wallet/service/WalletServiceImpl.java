package com.raggerbreak.bsipasswordwalletbe.wallet.service;

import com.raggerbreak.bsipasswordwalletbe.exceptions.PasswordAuthorizationException;
import com.raggerbreak.bsipasswordwalletbe.exceptions.WalletPasswordException;
import com.raggerbreak.bsipasswordwalletbe.security.model.User;
import com.raggerbreak.bsipasswordwalletbe.security.service.UserService;
import com.raggerbreak.bsipasswordwalletbe.wallet.dto.WalletPasswordDTO;
import com.raggerbreak.bsipasswordwalletbe.wallet.mapper.WalletPasswordMapper;
import com.raggerbreak.bsipasswordwalletbe.wallet.model.WalletPassword;
import com.raggerbreak.bsipasswordwalletbe.wallet.repository.WalletPasswordRepository;
import com.raggerbreak.bsipasswordwalletbe.wallet.web.request.UpdatePasswordRequest;
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
    public WalletPassword updatePassword(Long passwordId, UpdatePasswordRequest request) throws Exception {
        User currentUser = userService.getCurrentAuthUser();
        WalletPassword returnedPassword = walletPasswordRepository.findById(passwordId)
                .orElseThrow(() -> new NotFoundException("Password Not Found"));

        if (!returnedPassword.getUser().getId().equals(currentUser.getId())) {
            throw new PasswordAuthorizationException("You have to be an owner to update password");
        }

        String decryptedPassword = cryptoService.decrypt(returnedPassword.getPassword(), currentUser.getWalletPassword());
        if (!decryptedPassword.equals(request.getOldPassword())) {
            throw new WalletPasswordException("Your old password and confirmation password do not match");
        }

        returnedPassword.setName(request.getName());
        returnedPassword.setLogin(request.getLogin());
        returnedPassword.setDescription(request.getDescription());
        returnedPassword.setPassword(cryptoService.encrypt(request.getNewPassword(), userService.getCurrentAuthUser().getWalletPassword()));
        return walletPasswordRepository.save(returnedPassword);

    }

    @Override
    public PasswordResponse decodePassword(Long passwordId) throws Exception {
        User user = userService.getCurrentAuthUser();
        WalletPasswordDTO walletPasswordDTO = walletPasswordRepository.findById(passwordId)
                .map(walletPasswordMapper::walletPasswordToDTO)
                .orElseThrow(() -> new NotFoundException("Password Not Found"));

        if (walletPasswordDTO.getOwnerId().equals(user.getId()) || walletPasswordDTO.getSharedUsers()
                .stream().anyMatch(f -> f.getId().equals(user.getId()))) {
            return PasswordResponse.builder()
                    .password(cryptoService.decrypt(walletPasswordDTO.getPassword(), user.getWalletPassword()))
                    .build();

        } else {
            throw new PasswordAuthorizationException("You have to be an owner or shared user to decode password");
        }
    }

    @Override
    public void deletePassword(Long passwordId) throws Exception {
        User user = userService.getCurrentAuthUser();

        WalletPasswordDTO walletPasswordDTO = walletPasswordRepository.findById(passwordId)
                .map(walletPasswordMapper::walletPasswordToDTO)
                .orElseThrow(() -> new NotFoundException("Password Not Found"));

        if (walletPasswordDTO.getOwnerId().equals(user.getId())) {
            walletPasswordRepository.deleteById(passwordId);
        } else {
            throw new PasswordAuthorizationException("You have to be an owner to delete password");
        }

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
    public List<WalletPasswordDTO> getSharedPasswordsForCurrentUser() {
        User currentUser = userService.getCurrentAuthUser();
        return walletPasswordRepository.findAllBySharedUsers(currentUser).stream()
                .map(walletPasswordMapper::walletPasswordToDTOWithoutSharedUsers)
                .collect(Collectors.toList());
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
