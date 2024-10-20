package com.nikamilon.database.repositories;


import com.nikamilon.database.domain.entity.AuthorEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepositories extends CrudRepository<AuthorEntity, Long> {

    List<AuthorEntity> ageLessThan(Integer age);

    @Query("select a from AuthorEntity a where a.age > ?1")
    List<AuthorEntity> findAuthorWithAgeGreaterThan(Integer age);
}
