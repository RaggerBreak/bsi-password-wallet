package com.raggerbreak.bsipasswordwalletbe.wallet.web;

import com.raggerbreak.bsipasswordwalletbe.wallet.dto.WalletPasswordDTO;
import com.raggerbreak.bsipasswordwalletbe.wallet.service.WalletService;
import com.raggerbreak.bsipasswordwalletbe.wallet.web.response.CreatePasswordResponse;
import com.raggerbreak.bsipasswordwalletbe.wallet.web.response.PasswordResponse;
import com.raggerbreak.bsipasswordwalletbe.wallet.web.response.SharePasswordResponse;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<WalletPasswordDTO> getAllWalletPasswords(Principal principal) {
        return walletService.getAllWalletPasswords(principal.getName());
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public CreatePasswordResponse addWalletPassword(@Valid @RequestBody WalletPasswordDTO walletPasswordDTO) throws Exception  {
        return CreatePasswordResponse.builder()
                .walletPasswordId(walletService.addPassword(walletPasswordDTO).getId())
                .build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/password/{passwordId}")
    public PasswordResponse decodePassword(@PathVariable Long passwordId) throws Exception {
        return walletService.decodePassword(passwordId);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/password/{passwordId}")
    public void deletePassword(@PathVariable Long passwordId) throws Exception {
         walletService.deletePassword(passwordId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/password/share")
    public List<WalletPasswordDTO> getSharedPasswordsForUser(Principal principal) {
        return walletService.getSharedPasswordsForCurrentUser();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/password/share/{passwordId}/{userEmail}")
    public SharePasswordResponse sharePassword(@PathVariable Long passwordId, @PathVariable String userEmail) throws Exception {
        return walletService.sharePassword(passwordId, userEmail);
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/password/share/{passwordId}/{userId}")
    public void deleteUserFromSharedPassword(@PathVariable Long passwordId, @PathVariable Long userId) throws NotFoundException {
        walletService.deleteUserFromSharedPassword(passwordId, userId);
    }
}
