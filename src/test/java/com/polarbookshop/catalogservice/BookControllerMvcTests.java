package com.polarbookshop.catalogservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.polarbookshop.catalogservice.controller.BookController;
import com.polarbookshop.catalogservice.exceptions.BookNotFoundException;
import com.polarbookshop.catalogservice.service.BookService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
    Some integration tests do not require a fully initialized application context. 
    E.g. theres no need to load the web components when testing the data persistence layer. 
    If u're testing the web components, theres no need to load the data persistence layer. 
 */
// Identify a test class that focuses on Spring MVC component, explicitly targetting BookController
@WebMvcTest(BookController.class)
public class BookControllerMvcTests {

  // Utility class to test the web layer in a mock env
  @Autowired
  private MockMvc mockMvc;

  // Add a mock of BookService to the Spring application context
  // Mocks created with the MockBean are different from standard mocks (e.g. those
  // created with Mockito)
  // since the application context is also mocked, not only the class
  @MockBean
  private BookService bookService;

  @Test
  void whenGetBookNotExistingThenShouldReturn404() throws Exception {
    String isbn = "73737313940";
    given(bookService.viewBookDetails(isbn))
        // Define the expected behaviour for the BookService mock bean
        .willThrow(BookNotFoundException.class);
    mockMvc
        // MockMvc is used to perform a HTTP GET request and verify the result
        .perform(get("/books/" + isbn))
        // Expecting status code of 404
        .andExpect(status().isNotFound());
  }
}
