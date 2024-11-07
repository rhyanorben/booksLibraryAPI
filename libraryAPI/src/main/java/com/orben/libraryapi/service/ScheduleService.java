package com.orben.libraryapi.service;

import com.orben.libraryapi.model.entity.Loan;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private static final String CRON_LATE_LOANS = "0 0 0 1/1 * ?";

    @Value("${application.mail.lateloans.message}")
    private String message;

    private final LoanService loanService;
    private final EmailService emailService;

    @Scheduled(cron = CRON_LATE_LOANS)
    public void sendEmailToLoans(){
        List<Loan> allLateLoans = loanService.getAllLateLoan();
        List<String> mailsList = allLateLoans.stream().map(loan -> loan.getCustomerEmail()).collect(Collectors.toList());

        emailService.sendEmails(message, mailsList);
    }

}
