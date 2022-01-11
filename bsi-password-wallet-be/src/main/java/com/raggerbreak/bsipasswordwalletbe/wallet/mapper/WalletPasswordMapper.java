package com.raggerbreak.bsipasswordwalletbe.wallet.mapper;

import com.raggerbreak.bsipasswordwalletbe.wallet.dto.WalletPasswordDTO;
import com.raggerbreak.bsipasswordwalletbe.wallet.model.WalletPassword;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletPasswordMapper {
     WalletPasswordDTO walletPasswordToDTO(WalletPassword walletPassword);
}
