package com.orben.libraryapi.model.repository;

import com.orben.libraryapi.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado")
    public void returnTrueWhenIsbnExists() {

        Book book = new Book().builder()
                .title("Livro")
                .author("Autor")
                .isbn("123").build();
        entityManager.persist(book);

        assertThat(repository.existsByIsbn("123")).isTrue();

    }

    @Test
    @DisplayName("Deve retornar falso quando não existir um livro na base com o isbn informado")
    public void returnFalseWhenIsbnDoesNotExists() {
        assertThat(repository.existsByIsbn("123")).isFalse();
    }

    @Test
    @DisplayName("Deve obter o livro por ID")
    public void findByIdTest() {
        Book book = Book.builder().author("some author").title("some title").isbn("123").build();
        entityManager.persist(book);

        assertThat(repository.findById(book.getId()).isPresent()).isTrue();

    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest() {
        Book book = Book.builder().author("some author").title("some title").isbn("123").build();

        entityManager.persist(book);
        repository.delete(book);

        assertThat(repository.findById(book.getId()).isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {
        Book book = Book.builder().author("some author").title("some title").isbn("123").build();

        Book savedBook = repository.save(book);

        assertThat(savedBook.getId()).isNotNull();

    }

}
