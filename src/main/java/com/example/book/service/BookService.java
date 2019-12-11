package com.example.book.service;

import com.example.book.model.Book;
import com.example.book.persistence.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    public List<Book> filterBooksWith(Optional<String> author,
                                      Optional<String> genre,
                                      Optional<String> title,
                                      Optional<String> publicationDate,
                                      Optional<String> pageNumber) {
        List<Book> filteredBooks= new ArrayList<>();
        if (author.isPresent()) {
            filteredBooks.addAll(repository.findBooksByAuthor(author.get()));
        } else if (genre.isPresent()) {
            filteredBooks.addAll(repository.findBooksByGenre(genre.get()));
        } else if (title.isPresent()) {
            filteredBooks.addAll(repository.findBooksByTitle(title.get()));
        } else if (publicationDate.isPresent()) {
            filteredBooks.addAll(repository.findBooksByPublicationDate(publicationDate.get()));
        } else pageNumber.ifPresent(e -> filteredBooks.addAll(repository.findBooksByPageNumber(e)));
        return filteredBooks;
    }
}
