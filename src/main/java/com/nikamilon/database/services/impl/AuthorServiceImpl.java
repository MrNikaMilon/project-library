package com.nikamilon.database.services.impl;

import com.nikamilon.database.domain.dto.AuthorDto;
import com.nikamilon.database.domain.entity.AuthorEntity;
import com.nikamilon.database.repositories.AuthorRepositories;
import com.nikamilon.database.services.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.el.stream.Stream;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepositories authorRepositories;

    public AuthorServiceImpl(AuthorRepositories authorRepositories) {
        this.authorRepositories = authorRepositories;
    }

    @Override
    public AuthorEntity save(AuthorEntity authorEntity) {
        return authorRepositories.save(authorEntity);
    }

    @Override
    public List<AuthorEntity> findAll() {
       return StreamSupport
               .stream(authorRepositories
                       .findAll()
                       .spliterator(),
                       false)
               .collect(Collectors.toList());
    }

    @Override
    public Optional<AuthorEntity> findOne(Long id) {
        return authorRepositories.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return authorRepositories.existsById(id);
    }

    @Override
    public AuthorEntity partialUpdate(Long id, AuthorEntity authorEntity) {
        authorEntity.setId(id);
        return authorRepositories.findById(id).map(existingAuthor -> {
            Optional.ofNullable(authorEntity.getName()).ifPresent(existingAuthor::setName);
            Optional.ofNullable(authorEntity.getAge()).ifPresent(existingAuthor::setAge);
            return authorRepositories.save(existingAuthor);
        }).orElseThrow(() -> new RuntimeException("Author does not exist"));
    }

    @Override
    public void delete(Long id) {
        authorRepositories.deleteById(id);
    }
}
