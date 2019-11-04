package ru.naumen.javka.session;

import ru.naumen.javka.utils.ByteUtils;
import ru.naumen.javka.utils.TimeProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Optional;

public class StatelessSessionManager implements SessionManager {
    private String secret;
    private Long ageSeconds;
    private TimeProvider timeProvider;

    public StatelessSessionManager(String secret, Long ageSeconds, TimeProvider timeProvider) {
        this.secret = secret.substring(0, 16);
        this.ageSeconds = ageSeconds;
        this.timeProvider = timeProvider;
    }

    private String encryptAes(String value) throws Throwable {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return ByteUtils.encodeHexString(cipher.doFinal(value.getBytes()));
    }

    private String decryptAes(String value) throws Throwable {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return new String(cipher.doFinal(ByteUtils.hexStringToByte(value)));
    }

    @Override
    public Optional<Long> userId(String session) throws Throwable {
        String decoded = decryptAes(session);
        String[] splitted = decoded.split("-");
        Long decodedSeconds = Long.parseLong(splitted[1]);
        Long ageSec = Long.parseLong(splitted[2]);
        Long current = timeProvider.currentSeconds();
        if (current > decodedSeconds + ageSec)
            return Optional.empty();
        else
            return Optional.of(Long.parseLong(splitted[0]));
    }

    @Override
    public String session(Long userId) throws Throwable {
        return encryptAes(String.format("%d-%d-%d", userId, timeProvider.currentSeconds(), ageSeconds));
    }
}
