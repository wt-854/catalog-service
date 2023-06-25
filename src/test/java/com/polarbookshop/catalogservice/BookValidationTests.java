package com.polarbookshop.catalogservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.polarbookshop.catalogservice.entity.Book;

// javax.validation replaced with jakarta.validation
// https://stackoverflow.com/questions/75085412/spring-boot-3-0-package-javax-validation-does-not-exist
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/* 
  Unit Tests for field validation
  The business logic of an app is usually a sensible area to cover unit tests.
  In Catalog Service app, a good candidate for unit testing is the validation logic for the Book class. 
*/
public class BookValidationTests {

  private static Validator validator;

  // Identifies a block of code executed before all tests in the class
  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  // Identifies a test case
  @Test
  void whenAllFieldsAreCorrectThenValidateSucceeds() {
    // Create a book with valid ISBN
    var book = Book.of("1234567890", "Title", "Author", 9.90, "Polarsophia");
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    // Assert there is no validation error
    assertThat(violations).isEmpty();
  }

  @Test
  void whenIsbnDefinedButIncorrectThenValidationFails() {
    // Create a book with invalid ISBN
    var book = Book.of("a234567890", "Title", "Author", 9.90, "Polarsophia");
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    assertThat(violations).hasSize(1);
    // Assert that the violated validation constraint is about the incorrect ISBN
    assertThat(violations.iterator().next().getMessage())
        .isEqualTo("The ISBN format must be valid.");
  }

  @Test
  void whenAuthorIsNotDefinedThenValidationFails() {
    var book = Book.of("1234567890", "Title", "", 9.90, "Polarsophia");
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getMessage())
        .isEqualTo("The book author must be defined.");
  }

  @Test
  void whenPriceIsNotDefinedThenValidationFails() {
    var book = Book.of("1234567890", "Title", "Author", null, "Polarsophia");
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getMessage())
        .isEqualTo("The book price must be defined.");
  }

  @Test
  void whenPriceDefinedButZeroThenValidationFails() {
    var book = Book.of("1234567890", "Title", "Author", 0.0, "Polarsophia");
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getMessage())
        .isEqualTo("The book price must be greater than zero.");
  }

  @Test
  void whenPriceDefinedButNegativeThenValidationFails() {
    var book = Book.of("1234567890", "Title", "Author", -9.90, "Polarsophia");
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getMessage())
        .isEqualTo("The book price must be greater than zero.");
  }

}
