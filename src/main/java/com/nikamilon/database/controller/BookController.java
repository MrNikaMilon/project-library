package com.nikamilon.database.controller;

import com.nikamilon.database.domain.dto.BookDto;
import com.nikamilon.database.domain.entity.BookEntity;
import com.nikamilon.database.mappers.Mapper;
import com.nikamilon.database.services.BookService;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private final BookService bookService;

    private final Mapper<BookEntity, BookDto> bookMapper;

    public BookController(BookService bookService,  Mapper<BookEntity, BookDto> bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> createUpdateBook(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto) {
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        boolean bookExists = bookService.ifExists(isbn);
        BookEntity savedBookEntity = bookService.createUpdateBook(isbn, bookEntity);
        BookDto savedUpdateBookDto = bookMapper.mapTo(savedBookEntity);

        if(bookExists){
            return new ResponseEntity<>(savedUpdateBookDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(savedUpdateBookDto, HttpStatus.CREATED);
        }
    }

    @GetMapping(path = "/books")
    public Page<BookDto> createBook(Pageable pageable){
        Page<BookEntity> books = bookService.findAll(pageable);
        return books.map(bookMapper::mapTo);
    }

    @PatchMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> partialUpdateBook(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto) {

        if(!bookService.ifExists(isbn)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        BookEntity updatedBookEntity = bookService.partialUpdate(isbn, bookEntity);
        return new ResponseEntity<>(
                bookMapper.mapTo(updatedBookEntity),
                HttpStatus.OK);
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> getBook(@PathVariable("isbn") String isbn){
        Optional<BookEntity> foundBook = bookService.findOne(isbn);
        return foundBook.map(bookEntity -> {
            BookDto bookDto = bookMapper.mapTo(bookEntity);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity deleteBook(@PathVariable("isbn") String isbn){
        bookService.delete(isbn);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
