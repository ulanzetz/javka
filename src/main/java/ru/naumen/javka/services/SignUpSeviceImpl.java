package ru.naumen.javka.services;

import ru.naumen.javka.domain.User;
import ru.naumen.javka.exceptions.ValidationException;
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
    public Optional<String> signUp(String userName, String password) throws Throwable {
        if(password.length() < 6) {
            throw new ValidationException("Длина пароля должна превышать 6 символов");
        }
        if(userName.length() < 5) {
            throw new ValidationException("Длина имени должны превышать 5 символов");
        }
        Optional<User> maybeUser = userRepository.findByName(userName);
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            String hash = hash(password);
            if (user.getPasswordHash().equals(hash)) {
                return Optional.of(sessionManager.session(user.getId()));
            } else {
                return Optional.empty();
            }
        } else return
                Optional.of(sessionManager.session(userRepository.save(userName, hash(password))));
    }

    private String hash(String password) throws NoSuchAlgorithmException {
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update((password + salt).getBytes());
        return ByteUtils.encodeHexString(crypt.digest());
    }
}
