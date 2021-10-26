package com.raggerbreak.bsipasswordwalletbe.wallet.web;

import com.raggerbreak.bsipasswordwalletbe.wallet.dto.WalletPasswordDTO;
import com.raggerbreak.bsipasswordwalletbe.wallet.service.WalletService;
import com.raggerbreak.bsipasswordwalletbe.wallet.web.response.CreatePasswordResponse;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public CreatePasswordResponse addWalletPassword(@Valid @RequestBody WalletPasswordDTO walletPasswordDTO) throws Exception  {
        return CreatePasswordResponse.builder()
                .walletPasswordId(walletService.addPassword(walletPasswordDTO).getId())
                .build();
    }
}
