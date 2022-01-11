package com.raggerbreak.bsipasswordwalletbe.wallet.mapper;

import com.raggerbreak.bsipasswordwalletbe.wallet.dto.WalletPasswordDTO;
import com.raggerbreak.bsipasswordwalletbe.wallet.model.WalletPassword;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WalletPasswordMapper {

     @Mapping(target = "ownerId", source = "user.id")
     WalletPasswordDTO walletPasswordToDTO(WalletPassword walletPassword);

     @Mapping(target = "ownerId", source = "user.id")
     @Mapping(target = "sharedUsers", ignore = true)
     WalletPasswordDTO walletPasswordToDTOWithoutSharedUsers(WalletPassword walletPassword);
}
