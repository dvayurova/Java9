package edu.school21.sockets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepository;

import java.security.SecureRandom;
import java.util.Optional;

@Component("UsersServiceImpl")
public class UsersServiceImpl implements UsersService {

    @Autowired
    @Qualifier("UsersRepositoryImpl")
    private UsersRepository usersRepository;

    @Autowired
    @Qualifier("passwordEncoder")
    private PasswordEncoder passwordEncoder;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public void signUp(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setEmail(username);
        user.setPassword(encodedPassword);
        usersRepository.save(user);
    }

    @Override
    public User signIn(String username, String password) {
       Optional<User>  userOptional = usersRepository.findByEmail(username);
        if (userOptional.isEmpty()) return null;
        User user = userOptional.get();
        if (user.getEmail().equals(username) && passwordEncoder.matches(password, user.getPassword()))
            return user;
        return null;
    }


}
