package edu.school21.sockets.application;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import edu.school21.sockets.config.ApplicationConfig;
import edu.school21.sockets.repositories.UsersRepository;
import edu.school21.sockets.services.UsersService;

public class Main {
    public static void main(String[] args){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        UsersService usersService = context.getBean("UsersServiceImpl", UsersService.class);
        usersService.signUp("mail.ru");
        UsersRepository repository = context.getBean("UsersRepositoryImpl", UsersRepository.class);
        System.out.println(repository.findByEmail("mail.ru"));
    }
}


