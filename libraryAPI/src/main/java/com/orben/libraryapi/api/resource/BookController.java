package com.orben.libraryapi.api.resource;

import com.orben.libraryapi.api.dto.BookDTO;
import com.orben.libraryapi.api.dto.LoanDTO;
import com.orben.libraryapi.model.entity.Book;
import com.orben.libraryapi.model.entity.Loan;
import com.orben.libraryapi.service.BookService;
import com.orben.libraryapi.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;
    private final ModelMapper mapper;
    private final LoanService loanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create( @RequestBody @Valid BookDTO bookDTO ) {
        Book entity = service.save(mapper.map(bookDTO, Book.class));
        return mapper.map(entity, BookDTO.class);
    }

    @GetMapping("/{id}")
    public BookDTO findById(@PathVariable("id") Long id) {
        return service
                .getById(id)
                .map(book -> mapper.map(book, BookDTO.class))
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }

    @GetMapping
    public Page<BookDTO> find(BookDTO dto, Pageable pageRequest) {
        Book filter = mapper.map(dto, Book.class);
        Page<Book> result = service.find(filter, pageRequest);
        List<BookDTO> list = result.getContent().stream().map(entity -> mapper.map(entity, BookDTO.class)).collect(Collectors.toList());

        return new PageImpl<>(list, pageRequest, result.getTotalElements());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        Book book = service
                .getById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        service.delete(book);
    }

    @PutMapping("/{id}")
    public BookDTO update(@PathVariable Long id, @RequestBody @Valid BookDTO bookDTO) {
        return service
                .getById(id)
                .map(book -> {
                    book.setTitle(bookDTO.getTitle());
                    book.setAuthor(bookDTO.getAuthor());
                    book = service.update(book);
                    return mapper.map(book, BookDTO.class);
                } )
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }

    @GetMapping("/{id}/loans")
    public Page<LoanDTO> loansByBook( @PathVariable Long id, Pageable pageable) {
        Book book = service.getById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        Page<Loan> loansByBook = loanService.getLoansByBook(book, pageable);

        List<LoanDTO> list = loansByBook.getContent().stream().map(loan -> {
            Book loanBook = loan.getBook();
            BookDTO bookDTO = mapper.map(loanBook, BookDTO.class);
            LoanDTO loanDTO = mapper.map(loan, LoanDTO.class);
            loanDTO.setBook(bookDTO);
            return loanDTO;
        }).collect(Collectors.toList());

        return new PageImpl<>(list, pageable, loansByBook.getTotalElements());
    }

}
