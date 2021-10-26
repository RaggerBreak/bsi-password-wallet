package com.raggerbreak.bsipasswordwalletbe.wallet.service;

import com.raggerbreak.bsipasswordwalletbe.wallet.repository.WalletPasswordRepository;
import com.raggerbreak.bsipasswordwalletbe.wallet.dto.WalletPasswordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletPasswordRepository walletPasswordRepository;

    @Override
    public List<WalletPasswordDTO> getAllWalletPasswords(String username) {
        return walletPasswordRepository.findAllByUsername(username);
    }
}
