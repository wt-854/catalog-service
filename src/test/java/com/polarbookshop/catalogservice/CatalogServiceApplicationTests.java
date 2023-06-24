package com.polarbookshop.catalogservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.polarbookshop.catalogservice.entity.Book;

/*
		Integration tests cover the interaction among software components. 
		When working with web apps, u can run tests on a mock web env or a running server.
		When using mock web env, u can use MockMvc object to send HTTP req to the app to check the results. 
		For env with running server, the TestRestTemplate utility lets u perform REST calls to an app running 
		on an actual server. By inspecting the HTTP responses, you can verify that the API works as intended.
 */
// Loads a full Spring web app context and servlet container listening on a random port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class CatalogServiceApplicationTests {

	// Utility to perform REST calls for testing
	@Autowired
	private WebTestClient webTestClient;

	@Test
	void whenPostRequestThenBookCreated() {
		var expectedBook = Book.of("1231231231", "Title", "Author", 9.90);
		webTestClient
				// send POST request
				.post()
				// to "/books" endpoint
				.uri("/books")
				// add the book in the request body
				.bodyValue(expectedBook)
				// send the request
				.exchange()
				// verify that HTTP response is 201
				.expectStatus().isCreated()
				.expectBody(Book.class).value(actualBook -> {
					// verify that HTTP response has a non-null body
					assertThat(actualBook).isNotNull();
					// verify that object created is as expected
					assertThat(actualBook.getIsbn()).isEqualTo(expectedBook.getIsbn());
				});
	}

	// for hiding Potential null pointer access warning
	@SuppressWarnings("null")
	@Test
	void whenGetRequestWithIdThenBookReturned() {
		var bookIsbn = "1231231230";
		var bookToCreate = Book.of("1231231230", "Title", "Author", 9.90);
		Book expectedBook = webTestClient
				.post()
				.uri("/books")
				.bodyValue(bookToCreate)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class).value(book -> assertThat(book).isNotNull())
				.returnResult().getResponseBody();

		webTestClient
				.get()
				.uri("/books/" + bookIsbn)
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBody(Book.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
					assertThat(actualBook.getIsbn()).isEqualTo(expectedBook.getIsbn());
				});
	}

	// for hiding Potential null pointer access warning
	@SuppressWarnings("null")
	@Test
	void whenPutRequestThenBookUpdated() {
		var bookIsbn = "1231231232";
		var bookToCreate = Book.of(bookIsbn, "Title", "Author", 9.90);
		Book createdBook = webTestClient
				.post()
				.uri("/books")
				.bodyValue(bookToCreate)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class).value(book -> assertThat(book).isNotNull())
				.returnResult().getResponseBody();
		createdBook.setPrice(7.95);

		webTestClient
				.put()
				.uri("/books/" + bookIsbn)
				.bodyValue(createdBook)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Book.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
					assertThat(actualBook.getPrice()).isEqualTo(createdBook.getPrice());
				});
	}

	@Test
	void whenDeleteRequestThenBookDeleted() {
		var bookIsbn = "1231231233";
		var bookToCreate = Book.of(bookIsbn, "Title", "Author", 9.90);
		webTestClient
				.post()
				.uri("/books")
				.bodyValue(bookToCreate)
				.exchange()
				.expectStatus().isCreated();

		webTestClient
				.delete()
				.uri("/books/" + bookIsbn)
				.exchange()
				.expectStatus().isNoContent();

		webTestClient
				.get()
				.uri("/books/" + bookIsbn)
				.exchange()
				.expectStatus().isNotFound()
				.expectBody(String.class).value(
						errorMessage -> assertThat(errorMessage).isEqualTo("The book with ISBN " + bookIsbn + " was not found."));
	}
}
