package com.polarbookshop.catalogservice.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "The book ISBN must be defined.")
  @Pattern(regexp = "^([0-9]{10}|[0-9]{13})$", message = "The ISBN format must be valid.")
  public String isbn;

  @NotBlank(message = "The book title must be defined.")
  public String title;

  @NotBlank(message = "The book author must be defined.")
  public String author;

  @NotNull(message = "The book price must be defined.")
  @Positive(message = "The book price must be greater than zero.")
  public Double price;

  public String publisher;

  @CreatedDate
  private Instant createdDate;

  @LastModifiedDate
  private Instant lastModifiedDate;

  @Version
  private int version;

  public static Book of(String isbn, String title, String author, Double price, String publisher) {
    return new Book(null, isbn, title, author, price, publisher, null, null, 0);
  }

}
