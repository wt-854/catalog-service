package com.polarbookshop.catalogservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

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
    var book = new Book("1234567890", "Title", "Author", 9.90);
    // Verifying the parsing from Java to JSON, using the JsonPath format to
    // navigate the JSON object
    var jsonContent = json.write(book);
    assertThat(jsonContent).extractingJsonPathStringValue("@.isbn")
        .isEqualTo(book.isbn);
    assertThat(jsonContent).extractingJsonPathStringValue("@.title")
        .isEqualTo(book.title);
    assertThat(jsonContent).extractingJsonPathStringValue("@.author")
        .isEqualTo(book.author);
    assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
        .isEqualTo(book.price);
  }

  @Test
  void testDeserialize() throws Exception {
    // Define the JSON object using the Java text block feature
    var content = """
        {
          "isbn": "1234567890",
          "title": "Title",
          "author": "Author",
          "price": 9.90
        }
        """;
    // Verifies the parsing from JSON to Java
    assertThat(json.parse(content))
        .usingRecursiveComparison()
        .isEqualTo(new Book("1234567890", "Title", "Author", 9.90));
  }
}
