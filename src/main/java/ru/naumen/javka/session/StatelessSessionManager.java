package ru.naumen.javka.session;

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
        return encodeHexString(cipher.doFinal(value.getBytes()));
    }

    private String decryptAes(String value) throws Throwable {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return new String(cipher.doFinal(hexStringToByte(value)));
    }

    private String encodeHexString(byte[] byteArray) {
        StringBuilder hexStringBuffer = new StringBuilder();
        for (byte b : byteArray) {
            hexStringBuffer.append(byteToHex(b));
        }
        return hexStringBuffer.toString();
    }

    private String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    private byte[] hexStringToByte(String hexString) {
        if (hexString.length() % 2 == 1) {
            throw new IllegalArgumentException(
                    "Invalid hexadecimal String supplied.");
        }

        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
        }
        return bytes;
    }

    private byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if (digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: " + hexChar);
        }
        return digit;
    }

    @Override
    public Optional<Long> userId(String session) throws Throwable {
        String decoded = decryptAes(session);
        String[] splitted = decoded.split("-");
        Long decodedSeconds = Long.parseLong(splitted[1]);
        Long ageSec = Long.parseLong(splitted[2]);
        Long current = timeProvider.currentSeconds();
        if(current > decodedSeconds + ageSec)
            return Optional.empty();
        else
            return Optional.of(Long.parseLong(splitted[0]));
    }

    @Override
    public String session(Long userId) throws Throwable {
        return encryptAes(String.format("%d-%d-%d", userId, timeProvider.currentSeconds(), ageSeconds));
    }
}
