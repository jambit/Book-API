package com.example.book.persistence;

import com.example.book.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "book", path = "book")
public interface BookRepository extends MongoRepository<Book, String> {

    List<Book> findBooksByGenre(String genre);

    List<Book> findBooksByAuthor(String author);

    List<Book> findBooksByTitle(String title);

    List<Book> findBooksByPageNumber(String pageNumber);

    List<Book> findBooksByPublicationDate(String publicationDate);
}
