package com.orben.libraryapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String customer;

    @Column(name = "costumer_email")
    private String customerEmail;

    @JoinColumn(nullable = false, name = "id_book")
    @ManyToOne
    private Book book;

    @Column(nullable = false)
    private LocalDate loanDate;

    @Column
    private Boolean returned;

}
