package edu.school21.sockets.services;

import edu.school21.sockets.models.User;
import org.springframework.stereotype.Component;

@Component
public interface UsersService {
    User signUp(String username, String password);
    User signIn(String username, String password);
}
