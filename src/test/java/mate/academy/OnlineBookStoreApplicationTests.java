package mate.academy;

import mate.academy.config.CustomMySqlContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class OnlineBookStoreApplicationTests {

    @Container
    static CustomMySqlContainer mySqlContainer = CustomMySqlContainer.getInstance();

    @Test
    void contextLoads() {
    }
}
