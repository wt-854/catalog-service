package com.polarbookshop.catalogservice.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Book {

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

}
