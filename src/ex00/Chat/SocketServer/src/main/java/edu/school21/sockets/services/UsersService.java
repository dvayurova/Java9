package edu.school21.sockets.services;

import org.springframework.stereotype.Component;

@Component
public interface UsersService {
    String signUp(String email);
}
