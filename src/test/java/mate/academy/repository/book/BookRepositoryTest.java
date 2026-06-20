package mate.academy.repository.book;

import java.util.List;
import mate.academy.model.book.Book;
import mate.academy.repository.CustomMySqlContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Container
    static CustomMySqlContainer mySqlContainer = CustomMySqlContainer.getInstance();

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("""
        Find all books by category id when category exists
        """)
    @Sql(scripts = "classpath:database/books/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoriesId_WithExistingCategoryId_ReturnsOneBook() {
        List<Book> actual = bookRepository.findAllByCategoriesId(1L);

        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals("The Hobbit", actual.get(0).getTitle());
    }
}
