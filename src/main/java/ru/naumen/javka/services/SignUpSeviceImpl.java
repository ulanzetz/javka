package ru.naumen.javka.services;

import ru.naumen.javka.domain.User;
import ru.naumen.javka.repositories.UserRepository;
import ru.naumen.javka.session.SessionManager;
import ru.naumen.javka.utils.ByteUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class SignUpSeviceImpl implements SignUpService {
    private SessionManager sessionManager;
    private UserRepository userRepository;
    private String salt;

    public SignUpSeviceImpl(SessionManager sessionManager, UserRepository userRepository, String salt) {
        this.sessionManager = sessionManager;
        this.userRepository = userRepository;
        this.salt = salt;
    }

    @Override
    public Optional<String> session(Long userId, String password) throws Throwable {
        User user = userRepository.findOne(userId);
        String hash = hash(password);
        if (user.getPasswordHash().equals(hash)) {
            return Optional.of(sessionManager.session(userId));
        } else {
            return Optional.empty();
        }
    }

    public User create(String name, String password) throws NoSuchAlgorithmException{
        User user = new User(name, hash(password));
        return userRepository.save(user);
    }

    private String hash(String password) throws NoSuchAlgorithmException {
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update((password + salt).getBytes());
        return ByteUtils.encodeHexString(crypt.digest());
    }
}
