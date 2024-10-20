package com.nikamilon.database.mappers.impl;

import com.nikamilon.database.domain.dto.AuthorDto;
import com.nikamilon.database.domain.dto.BookDto;
import com.nikamilon.database.domain.entity.AuthorEntity;
import com.nikamilon.database.domain.entity.BookEntity;
import com.nikamilon.database.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookMapperImpl implements Mapper<BookEntity, BookDto> {

    private final ModelMapper modelMapper;

    public BookMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public BookDto mapTo(BookEntity bookEntity) {
        return modelMapper.map(bookEntity, BookDto.class);
    }

    @Override
    public BookEntity mapFrom(BookDto bookDto) {
        return modelMapper.map(bookDto, BookEntity.class);
    }
}
