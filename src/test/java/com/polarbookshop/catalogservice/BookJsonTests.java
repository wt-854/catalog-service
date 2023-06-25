package com.polarbookshop.catalogservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import com.polarbookshop.catalogservice.entity.Book;

/*
    Testing the JSON serialization of the domain objects
    Default JSON mapper used is Jackson
 */
// Identify a test class that focuses on JSON serialization
@JsonTest
public class BookJsonTests {

        // Utility class to assert JSON serialization and deserialization
        @Autowired
        private JacksonTester<Book> json;

        @Test
        void testSerialize() throws Exception {
                var now = Instant.now();
                var book = new Book(394L, "1234567890", "Title", "Author", 9.90, "Polarsophia", now, now, 21);
                // Verifying the parsing from Java to JSON, using the JsonPath format to
                // navigate the JSON object
                var jsonContent = json.write(book);
                assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
                                .isEqualTo(book.getId().intValue());
                assertThat(jsonContent).extractingJsonPathStringValue("@.isbn")
                                .isEqualTo(book.getIsbn());
                assertThat(jsonContent).extractingJsonPathStringValue("@.title")
                                .isEqualTo(book.getTitle());
                assertThat(jsonContent).extractingJsonPathStringValue("@.author")
                                .isEqualTo(book.getAuthor());
                assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
                                .isEqualTo(book.getPrice());
                assertThat(jsonContent).extractingJsonPathStringValue("@.createdDate")
                                .isEqualTo(book.getCreatedDate().toString());
                assertThat(jsonContent).extractingJsonPathStringValue("@.lastModifiedDate")
                                .isEqualTo(book.getLastModifiedDate().toString());
                assertThat(jsonContent).extractingJsonPathNumberValue("@.version")
                                .isEqualTo(book.getVersion());
        }

        @Test
        void testDeserialize() throws Exception {
                // Define the JSON object using the Java text block feature
                var instant = Instant.parse("2021-09-07T22:50:37.135029Z");
                var content = """
                                {
                                    "id": 394,
                                    "isbn": "1234567890",
                                    "title": "Title",
                                    "author": "Author",
                                    "price": 9.90,
                                    "publisher": "Polarsophia",
                                    "createdDate": "2021-09-07T22:50:37.135029Z",
                                    "lastModifiedDate": "2021-09-07T22:50:37.135029Z",
                                    "version": 21
                                }
                                """;
                // Verifies the parsing from JSON to Java
                assertThat(json.parse(content))
                                .usingRecursiveComparison()
                                .isEqualTo(new Book(394L, "1234567890", "Title", "Author", 9.90, "Polarsophia", instant, instant, 21));
        }
}
