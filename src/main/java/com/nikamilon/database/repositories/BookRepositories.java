package com.nikamilon.database.repositories;

import com.nikamilon.database.domain.entity.BookEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepositories  extends CrudRepository<BookEntity, String>,
        PagingAndSortingRepository<BookEntity, String> {

}
