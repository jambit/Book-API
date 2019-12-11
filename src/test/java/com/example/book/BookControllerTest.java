package com.example.book;

import com.example.book.model.Book;
import com.example.book.persistence.BookRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository repository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Book generateBook(String title, String genre, String author, String publicationDate, String pageNumber) {
        final Book book = new Book();
        book.setAuthor(author);
        book.setGenre(genre);
        book.setPageNumber(pageNumber);
        book.setTitle(title);
        book.setPublicationDate(publicationDate);
        return book;
    }

    @Test
    void showAllBooks_findAllBooks_returnListOfBooksWithThreeBooks() throws Exception {
        final List<Book> bookList = Arrays.asList(
                generateBook("Sprite", "Fantasy", "Abassin", "01.01.2019", "130"),
                generateBook("Fanta", "Sci-Fi", "Hans Mustermann", "01.01.1902", "10"),
                generateBook("Cider", "Kochbuch", "Abassin Saleh", "12.12.1902", "40")
        );

        when(repository.findAll()).thenReturn(bookList);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/books/all")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        List<Book> returnedBooks = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<Book>>() {
                }
        );
        Assertions.assertIterableEquals(bookList, returnedBooks);
        verify(repository).findAll();
    }

    @Test
    void showAllBooks_findAllBooks_returnEmptyList() throws Exception {
        when(repository.findAll()).thenReturn(emptyList());
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/books/all")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        List<Book> returnedBooks = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<Book>>() {
                }
        );
        assertIterableEquals(emptyList(), returnedBooks);
        verify(repository).findAll();
    }

    @Test
    void createNewBook_saveANewBook_newBookIsSaved() throws Exception {
        final Book book = generateBook("Sprite", "Fantasy", "Abassin", "01.01.2019", "130");

        when(repository.save(book)).thenReturn(book);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/books")
                        .content(objectMapper.writeValueAsString(book))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        Book returnedBook = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                Book.class
        );
        assertEquals(book, returnedBook);
        verify(repository).save(book);
    }

    @Test
    void createNewBook_saveANewBook_returnsBadRequestWrongPublicationDate() throws Exception {
        final Book bookWithInvalidPublicationDate = generateBook("PepsiMax", "Wissen", "Abassin Saleh", "99.99.2001", "110");

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/books")
                        .content(objectMapper.writeValueAsString(bookWithInvalidPublicationDate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNewBook_saveANewBook_savesBookWithAuthorAndTitle() throws Exception {
        final Book bookWithAuthorAndTitle = generateBook("Hansel and Gretel", null, "Max Mustermann", null, null);

        when(repository.save(bookWithAuthorAndTitle)).thenReturn(bookWithAuthorAndTitle);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/books")
                        .content(objectMapper.writeValueAsString(bookWithAuthorAndTitle))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        Book returnedBook = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                Book.class
        );
        assertEquals(bookWithAuthorAndTitle, returnedBook);
    }

    @Test
    void createNewBook_saveANewBook_returnsBadRequestEmptyBook() throws Exception {
        final Book emptyBook = new Book();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/books")
                        .content(objectMapper.writeValueAsString(emptyBook))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void createNewBook_saveANewBook_returnsBadRequestBookWithoutAuthorAndTitle() throws Exception {
        final Book bookWithoutAuthorAndTitle = generateBook(null, "Fantasy", null, "20.20.2020", "220");

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/books")
                        .content(objectMapper.writeValueAsString(bookWithoutAuthorAndTitle))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void showNumberOfBooks_findAll_returnsAmountOfBooks() throws Exception {
        when(repository.count()).thenReturn(2L);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/books/size")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        long size = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), long.class);

        assertEquals(2L, size);
    }

    @Test
    void deleteBook_deleteById_deletesBook() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void filterBooks_getABookWithGenreFantasy_returnsBookWithGenreFantasy() throws Exception {
        final List<Book> bookList = Arrays.asList(
                generateBook("Sprite", "Fantasy", "Abassin", "01.01.2019", "130"),
                generateBook("Fanta", "Fantasy", "Hans Mustermann", "01.01.1902", "10")
        );

        when(repository.findBooksByGenre("Fantasy")).thenReturn(bookList);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/books?genre=Fantasy")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        List<Book> returnedBooks = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<Book>>() {
                }
        );
        assertEquals(bookList, returnedBooks);
    }

    @Test
    void filterBooks_getABookWithAuthorAbassin_returnsBookWithAuthorAbassin() throws Exception {
        final List<Book> authorFilteredList = Arrays.asList(
                generateBook("Sprite", "Sci-Fi", "Abass", "01.01.2019", "130"),
                generateBook("Fanta", "Sci-Fi", "Abass", "01.01.2019", "130")
        );

        when(repository.findBooksByAuthor("Abass")).thenReturn(authorFilteredList);

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/books?author=Abass")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        List<Book> returnedBook = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<Book>>() {
                }
        );
        assertEquals(authorFilteredList, returnedBook);
    }

    @Test
    void filterBooks_getABookWithAuthorNotExistingAuthor_returnsEmptyList() throws Exception {
        when(repository.findBooksByGenre("bread")).thenReturn(emptyList());

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/books?author=bread")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        List<Book> returnedBooks = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<Book>>() {
                }
        );
        assertEquals(emptyList(), returnedBooks);

    }

    @Test
    void filterBooks_getABookWithSpecificPublicationDate_returnsBookWithSpecificPublicationDate() throws Exception {
        final List<Book> publicationDateFilteredList = Arrays.asList(
                generateBook("Sprite", "Sci-Fi", "Abassin", "02.01.2019", "130"),
                generateBook("Fanta", "Sci-Fi", "Abassin", "02.01.2019", "130")
        );

        when(repository.findBooksByPublicationDate("01.01.2019")).thenReturn(publicationDateFilteredList);

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/books?publicationDate=01.01.2019")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        List<Book> returnedBooks = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<Book>>() {
                }
        );
        assertEquals(publicationDateFilteredList, returnedBooks);
    }

    @Test
    void filterBooks_getABookWithTitleFanta_returnsBookWithTitleFanta() throws Exception {
        final List<Book> publicationDateFilteredList = Arrays.asList(
                generateBook("Fanta", "Sci-Fi", "Abassin", "02.01.2019", "130"),
                generateBook("Fanta", "Sci-Fi", "Abassin", "02.01.2019", "130")
        );

        when(repository.findBooksByTitle("Fanta")).thenReturn(publicationDateFilteredList);

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/books?title=Fanta")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        List<Book> returnedBooks = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<Book>>() {
                }
        );
        assertEquals(publicationDateFilteredList, returnedBooks);
    }

    @Test
    void filterBooks_getABookWithpageNumber_returnsBookWithPageNumber() throws Exception {
        final List<Book> publicationDateFilteredList = Arrays.asList(
                generateBook("Fanta", "Sci-Fi", "Abassin", "02.01.2019", "140"),
                generateBook("Fanta", "Sci-Fi", "Abassin", "02.01.2019", "140")
        );

        when(repository.findBooksByPageNumber("140")).thenReturn(publicationDateFilteredList);

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/books?pageNumber=140")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        List<Book> returnedBooks = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<Book>>() {
                }
        );
        assertEquals(publicationDateFilteredList, returnedBooks);
    }
}