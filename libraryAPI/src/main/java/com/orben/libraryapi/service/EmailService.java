package com.orben.libraryapi.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmailService {
    void sendEmails(String message, List<String> mailsList);
}
