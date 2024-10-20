package com.nikamilon.database.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikamilon.database.TestDataUtil;
import com.nikamilon.database.domain.dto.BookDto;
import com.nikamilon.database.domain.entity.BookEntity;
import com.nikamilon.database.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    private BookService bookService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public BookControllerIntegrationTest(MockMvc mockMvc, BookService bookService) {
        this.mockMvc = mockMvc;
        this.bookService = bookService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateBookSuccessfullyReturnHttp201Created() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        String createBookJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatUpdateBooksSuccessfullyReturnSavedAuthors() throws Exception {
        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(null);
        BookEntity updateBook = bookService.createUpdateBook(
                testBookEntityA.getIsbn(), testBookEntityA
        );

        BookDto testBookDtoA = TestDataUtil.createTestBookDtoA(null);
        testBookDtoA.setIsbn(updateBook.getIsbn());
        String createBookJson = objectMapper.writeValueAsString(testBookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + updateBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUpdateBooksReturnUpdateBook() throws Exception {
        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(null);
        BookEntity updateBook = bookService.createUpdateBook(
                testBookEntityA.getIsbn(), testBookEntityA
        );

        BookDto testBookDtoA = TestDataUtil.createTestBookDtoA(null);
        testBookDtoA.setIsbn(updateBook.getIsbn());
        testBookDtoA.setTitle("UPDATE");
        String createBookJson = objectMapper.writeValueAsString(testBookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + updateBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value("978-1-2345-6789-0")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("UPDATE")
        );
    }

    @Test
    public void testThatListBooksReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books" )
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListBooksReturnsBook() throws Exception {
        BookEntity testBookDtoA = TestDataUtil.createTestBookEntityA(null);
        bookService.createUpdateBook("978-1-2345-6789-1", testBookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books" )
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].isbn").value(testBookDtoA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].title").value("The Shadow in the Attic")
        );
    }

    @Test
    public void testThatGetBookReturnHttpStatus200WhenBookExists() throws Exception {
        BookEntity testBookDtoA = TestDataUtil.createTestBookEntityA(null);
        bookService.createUpdateBook("978-1-2345-6789-1", testBookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" +testBookDtoA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetBookReturnHttpStatus404WhenBookDoesNoExists() throws Exception {
        BookEntity testBookDtoA = TestDataUtil.createTestBookEntityA(null);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" +testBookDtoA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateBookReturnHttpStatus200Ok() throws Exception {
        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(null);
        bookService.createUpdateBook(
                testBookEntityA.getIsbn(), testBookEntityA
        );

        BookDto testBookDtoA = TestDataUtil.createTestBookDtoA(null);
        testBookDtoA.setTitle("UPDATE");
        String bookJson = objectMapper.writeValueAsString(testBookDtoA);


        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + testBookEntityA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateBookReturnUpdateBook() throws Exception {
        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(null);
        bookService.createUpdateBook(
                testBookEntityA.getIsbn(), testBookEntityA
        );

        BookDto testBookDtoA = TestDataUtil.createTestBookDtoA(null);
        testBookDtoA.setTitle("UPDATE");
        String bookJson = objectMapper.writeValueAsString(testBookDtoA);


        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + testBookEntityA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookEntityA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("UPDATE")
        );
    }

    @Test
    public void testThatDeleteNonExistingBookReturnsHttpStatus204NoContent() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/" + "gjtugjtg")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect( MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteExistingBookReturnsHttpStatus204NoContent() throws Exception {
        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(null);
        bookService.createUpdateBook(
                testBookEntityA.getIsbn(), testBookEntityA
        );

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/" + testBookEntityA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect( MockMvcResultMatchers.status().isNoContent());
    }
}
