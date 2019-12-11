package com.example.book.api;

import com.example.book.model.Book;
import com.example.book.persistence.BookRepository;
import com.example.book.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Slf4j
@RestController
@RequestMapping("/api/v1/books")
@ComponentScan("com.example.book")
public class BookController {

    private final BookRepository repository;

    private final BookService bookService;

    @Autowired
    public BookController(BookRepository repository, BookService bookService) {
        this.repository = repository;
        this.bookService = bookService;
    }


    @ApiOperation(
            value = "Creates a new book",
            notes = "Creates an object of Type book which is added to the Database",
            response = Book.class)
    @PostMapping
    public ResponseEntity<String> createNewBook(
            @ApiParam(value = "An Object of type Book that you want to add to the Database", required = true)
            @Valid @RequestBody Book newBook) {
        if (newBook.getTitle() == null || newBook.getAuthor() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Author or Title is missing");
        }
        try {
            Book testBook = repository.save(newBook);
            String test = new ObjectMapper().writeValueAsString(testBook);
            return ResponseEntity.status(HttpStatus.CREATED).body(test);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not parse book as json");
        }
    }

    @ApiOperation(
            value = "Shows all books",
            notes = "Provides a List of type Book with all books saved in the Database",
            response = Book.class,
            responseContainer = "List")
    //TODO: Collections in Java angucken unterschiedliche Perfomance, O-Notation
    @GetMapping("/all")
    public ResponseEntity<List<Book>> showAllBooks() {
        return ResponseEntity.ok(repository.findAll());
    }

    @ApiOperation(
            value = "Shows the total amount of books",
            notes = "Provides the total amount of elements saved in the Database"
    )
    @GetMapping("/size")
    public ResponseEntity<Long> showNumberOfBooks() {
        return ResponseEntity.ok(repository.count());
    }

    @ApiOperation(
            value = "Filter all books",
            notes = "Filtering through all books and returning the sorted list of books.",
            response = Book.class,
            responseContainer = "List"
    )
    @GetMapping
    public ResponseEntity<List<Book>> filterBooks(@RequestParam(required = false) Optional<String> author,
                                                  @RequestParam(required = false) Optional<String> genre,
                                                  @RequestParam(required = false) Optional<String> title,
                                                  @RequestParam(required = false) Optional<String> publicationDate,
                                                  @RequestParam(required = false) Optional<String> pageNumber) {
        if (!author.isPresent() && !genre.isPresent() && !title.isPresent() && !publicationDate.isPresent() && !pageNumber.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameter is invalid or not supplied");
        }
        final List<Book> filteredBooks = bookService.filterBooksWith(author, genre, title, publicationDate, pageNumber);
        return ResponseEntity.ok(filteredBooks);
    }

    @ApiOperation(
            value = "Deletes a book",
            notes = "An object of type book is deleted by handing over an _id"
    )
    @DeleteMapping("/{_id}")
    public ResponseEntity<?> deleteBook(
            @ApiParam(value = "An _id that's used to identify a book in the database")
            @PathVariable String _id) {
        repository.deleteById(_id);
        return ResponseEntity.ok("{}");
    }
}
