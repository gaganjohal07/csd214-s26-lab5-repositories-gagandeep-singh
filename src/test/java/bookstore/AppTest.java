package bookstore;

import bookstore.pojos.*;
import bookstore.repositories.InMemoryListRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    private final InputStream originalSystemIn = System.in;

    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn);
    }

    @Test
    void testAppFlow_AddAndEditBook() {
        StringBuilder script = new StringBuilder();

        script.append("1\n");
        script.append("1\n");
        script.append("Dune\n");
        script.append("Frank Herbert\n");
        script.append("10\n");
        script.append("25.00\n");
        script.append("99\n");

        script.append("2\n");
        script.append("0\n");
        script.append("Dune Messiah\n");
        script.append("\n");
        script.append("\n");
        script.append("\n");

        script.append("99\n");

        System.setIn(new ByteArrayInputStream(script.toString().getBytes()));

        InMemoryListRepository repository = new InMemoryListRepository();

        App app = new App(repository) {
            @Override
            public void populate() {
            }
        };

        app.run();

        assertEquals(1, repository.count());

        Book result = (Book) repository.findAll()
                .stream()
                .findFirst()
                .map(entity -> Book.fromEntity((bookstore.entities.BookEntity) entity))
                .orElse(null);

        assertNotNull(result);
        assertEquals("Dune Messiah", result.getTitle());
    }
}