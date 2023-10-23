package edu.school21.sockets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepository;

import java.security.SecureRandom;

@Component("UsersServiceImpl")
public class UsersServiceImpl implements UsersService {

    @Autowired
    @Qualifier("UsersRepositoryImpl")
    private UsersRepository usersRepository;

    private final int PASSWORD_LENGTH = 8;
    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@-_?.";



    public UsersServiceImpl( UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public String signUp(String email) {
        String password = generatePassword();
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        usersRepository.save(user);
        return password;
    }

    private String generatePassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }

}
