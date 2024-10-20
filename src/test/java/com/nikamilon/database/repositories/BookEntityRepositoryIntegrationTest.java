package com.nikamilon.database.repositories;

import com.nikamilon.database.TestDataUtil;
import com.nikamilon.database.domain.entity.AuthorEntity;
import com.nikamilon.database.domain.entity.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public final class BookEntityRepositoryIntegrationTest {

    private final BookRepositories underTest;

    @Autowired
    public BookEntityRepositoryIntegrationTest(BookRepositories underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled(){
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        BookEntity bookEntity = TestDataUtil.createTestBookA(authorEntity);
        underTest.save(bookEntity);
        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());

        assertThat(result)
                .isPresent();
        assertThat(result.get())
                .isEqualTo(bookEntity);
    }

    @Test
    public void testThatMultipleBookCanBeCreatedAndRecalled(){
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();

        BookEntity bookEntityA = TestDataUtil.createTestBookA(authorEntityA);
        underTest.save(bookEntityA);

        BookEntity bookEntityB = TestDataUtil.createTestBookB(authorEntityA);
        underTest.save(bookEntityB);

        BookEntity bookEntityC = TestDataUtil.createTestBookC(authorEntityA);
        underTest.save(bookEntityC);

        Iterable<BookEntity> nooks = underTest.findAll();

        assertThat(nooks)
                .hasSize(3)
                .containsExactly(bookEntityA, bookEntityB, bookEntityC);
    }

    @Test
    public void testThatBookCanBeUpdated(){
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();

        BookEntity bookEntity = TestDataUtil.createTestBookA(authorEntity);
        underTest.save(bookEntity);

        bookEntity.setTitle("UPDATED");
        underTest.save(bookEntity);

        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookEntity);
    }

    @Test
    public void testThatBookCanBeDeleted(){
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        BookEntity bookEntity = TestDataUtil.createTestBookA(authorEntity);
        underTest.save(bookEntity);

        underTest.deleteById(bookEntity.getIsbn());
        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());
        assertThat(result).isEmpty();
    }


}
