package com.orben.libraryapi.service.impl;

import com.orben.libraryapi.api.dto.LoanFilterDTO;
import com.orben.libraryapi.exception.BusinessException;
import com.orben.libraryapi.model.entity.Book;
import com.orben.libraryapi.model.entity.Loan;
import com.orben.libraryapi.model.repository.LoanRepository;
import com.orben.libraryapi.service.LoanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {

     final LoanRepository repository;

    public LoanServiceImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        if( repository.existsByBookAndNotReturned(loan.getBook()) ){
            throw new BusinessException("Book already loaned");
        }
        return this.repository.save(loan);
    }

    @Override
    public Optional<Loan> getById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public Loan update(Loan loan) {
        return repository.save(loan);
    }

    @Override
    public Page<Loan> find(LoanFilterDTO filterDTO, Pageable pageable) {
        return repository.findByBookIsbnOrCustomer(filterDTO.getIsbn(), filterDTO.getCustomer(), pageable);
    }

    @Override
    public Page<Loan> getLoansByBook(Book book, Pageable pageable) {
        return repository.findByBook(book, pageable);
    }

    @Override
    public List<Loan> getAllLateLoan() {
        final Integer loanDays = 4;
        LocalDate threeDaysAgo = LocalDate.now().minusDays(loanDays);
        return repository.findByLoanDateLessThanAndNotReturned(threeDaysAgo);
    }
}
