package com.orben.libraryapi;

import com.orben.libraryapi.service.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class LibraryApiApplication {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CommandLineRunner runner(EmailService emailService) {
        return args -> {
            List<String> emails = Arrays.asList("=d671a718df-67dd99+1@inbox.mailtrap.io");
            emailService.sendEmails("Testando serviço de e-mails.", emails);
            System.out.println("Emails enviados");
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(LibraryApiApplication.class, args);
    }

}
