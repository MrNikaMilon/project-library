package com.nikamilon.database.services.impl;

import com.nikamilon.database.domain.entity.BookEntity;
import com.nikamilon.database.repositories.BookRepositories;
import com.nikamilon.database.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepositories bookRepositories;

    public BookServiceImpl(BookRepositories bookRepositories) {
        this.bookRepositories = bookRepositories;
    }

    @Override
    public BookEntity createUpdateBook(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);
        return bookRepositories.save(bookEntity);
    }

    @Override
    public List<BookEntity> findAll() {
        return StreamSupport
                .stream(bookRepositories
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BookEntity> findAll(Pageable pageable) {
        return bookRepositories.findAll(pageable);
    }

    @Override
    public Optional<BookEntity> findOne(String isbn) {
        return bookRepositories.findById(isbn);
    }

    @Override
    public boolean ifExists(String isbn) {
        return bookRepositories.existsById(isbn);
    }

    @Override
    public BookEntity partialUpdate(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);

        return bookRepositories.findById(isbn).map(existingBook -> {
                    Optional.ofNullable(bookEntity.getTitle()).ifPresent(existingBook::setTitle);
                    return bookRepositories.save(existingBook);
        }).orElseThrow(() -> new RuntimeException("Book does not exists"));
    }

    @Override
    public void delete(String isbn) {
        bookRepositories.deleteById(isbn);
    }
}
