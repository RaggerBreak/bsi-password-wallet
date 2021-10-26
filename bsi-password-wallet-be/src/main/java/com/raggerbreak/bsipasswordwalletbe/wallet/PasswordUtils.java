package com.raggerbreak.bsipasswordwalletbe.wallet;


import com.raggerbreak.bsipasswordwalletbe.security.model.PasswordForm;
import com.raggerbreak.bsipasswordwalletbe.security.model.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;


public final class PasswordUtils {
    private PasswordUtils() {}

    public static String encode(User user) {

        if (!Objects.isNull(user.getPasswordForm()) && user.getPasswordForm().equals(PasswordForm.SHA512))
            return calculateSHA512(user.getPassword(), user.getPassword());

        if (user.getPasswordForm().equals(PasswordForm.HMAC))
            return calculateHMAC(user.getPassword(), user.getPassword());

        throw new NotImplementedException("Calculate for PasswordForm: " + user.getPassword() + " not implemented");
    }

    private static String calculateSHA512(String password, String salt) {
        return DigestUtils.sha512Hex(addSalt(password, salt));
    }

    private static String calculateHMAC(String password, String key) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_512, key).hmacHex(password);
    }

    private static String addSalt(String password, String salt) {
        return StringUtils.join(salt, password, salt);
    }
}
