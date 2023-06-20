package com.polarbookshop.catalogservice.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.polarbookshop.catalogservice.entity.Book;

import jakarta.transaction.Transactional;

public interface BookRepository extends CrudRepository<Book, Long> {

  Optional<Book> findByIsbn(String isbn);

  boolean existsByIsbn(String isbn);

  @Transactional
  void deleteByIsbn(String isbn);

}
