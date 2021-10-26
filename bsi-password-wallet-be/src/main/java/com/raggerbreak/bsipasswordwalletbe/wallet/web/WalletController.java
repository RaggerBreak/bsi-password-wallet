package com.raggerbreak.bsipasswordwalletbe.wallet.web;

import com.raggerbreak.bsipasswordwalletbe.wallet.dto.WalletPasswordDTO;
import com.raggerbreak.bsipasswordwalletbe.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
