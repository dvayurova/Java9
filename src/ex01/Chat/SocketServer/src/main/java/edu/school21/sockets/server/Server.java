package edu.school21.sockets.server;

import edu.school21.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.ServerSocket;
import java.net.Socket;

@Component("Server")
public class Server {

    @Autowired
    @Qualifier("UsersServiceImpl")
    private UsersService usersService;

    public Server() {
    }

    public Server(UsersService usersService) {
        this.usersService = usersService;
    }

    public void start(int port) {

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Hello from server!");
            while(true) {
                if (!executeMethod(in.readLine(), out, in)) {
                    socket.close();
                    break;
                }
            }
            in.close();
            out.close();
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    private boolean executeMethod(String methodName, PrintWriter out, BufferedReader in) throws IOException, InvocationTargetException, IllegalAccessException {
        Method[] methods = usersService.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().contains(methodName)) {
                Object[] params = getParametersForMethodCall(method, out, in);
                Object result = method.invoke(usersService, params);
                if (method.getReturnType() == boolean.class && !(boolean)result) {
                    out.println("Incorrect username or password");
                    return false;
                } else{
                    sendResult( methodName,  out);
                    return true;
                }

            }
        }
        return true;
    }

    private static Object[] getParametersForMethodCall(Method method, PrintWriter out, BufferedReader in) throws InvocationTargetException, IllegalAccessException, IOException {
        Parameter[] parametersName = method.getParameters();
        Object[] parameters = new Object[parametersName.length];
        int i = 0;
        for (Parameter parameter : parametersName) {
            out.println("Enter " + parameter.getName().substring(parameter.getName().indexOf(" ") + 1) + ":");
            parameters[i] = in.readLine();
            i++;
        }
        return parameters;
    }

    private void sendResult(String methodName, PrintWriter out) {
        if (methodName.equals("signUp")) {
            out.println("Successful!");
        } else if (methodName.equals("signIn")) {
            out.println("Start messaging");
        } else if (methodName.equals("Exit")) {
            out.println("You have left the chat.");
        }
    }

}
