package edu.school21.sockets.helpers;

import edu.school21.sockets.models.User;
import edu.school21.sockets.services.UsersService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Authorization {

    private final PrintWriter out;
    private final BufferedReader in;
    private final UsersService usersService;
    private User user = null;

    public Authorization(PrintWriter out, BufferedReader in, UsersService usersService) {
        this.out = out;
        this.in = in;
        this.usersService = usersService;
    }

    public User authorise(String input) throws IOException {
        if (input.equals("signUp")) {
            signUp();
        } else if (input.equals("signIn")) {
            signIn();
        }
        return user;
    }

    private void signUp() throws IOException {
        String username = inputUsername();
        String password = inputPassword();
        usersService.signUp(username, password);
        out.println("Successful!");
    }

    private void signIn() throws IOException {
        String username = inputUsername();
        String password = inputPassword();
        user = usersService.signIn(username, password);
        if (user != null) {
            out.println("You have successfully logged in");
        } else {
            out.println("Incorrect username or password");
        }
    }

    private String inputUsername() throws IOException {
        out.println("Enter username:");
        return in.readLine();
    }

    private String inputPassword() throws IOException {
        out.println("Enter password:");
        return in.readLine();
    }

}
