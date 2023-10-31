package edu.school21.sockets.application;

import edu.school21.sockets.server.Server;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import edu.school21.sockets.config.SocketsApplicationConfig;


public class Main {
    public static void main(String[] args) {
        if (args.length != 1 || !args[0].startsWith("--port=")) {
            System.err.println("Please enter a port value in format \"--port=8081\"");
            System.exit(-1);
        }

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
        Server server = context.getBean("Server", Server.class);
        server.start(Integer.parseInt(args[0].substring(args[0].indexOf('=') + 1)));

    }
}


