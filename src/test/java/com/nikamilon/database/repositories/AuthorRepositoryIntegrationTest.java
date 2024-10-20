package com.nikamilon.database.repositories;

import com.nikamilon.database.TestDataUtil;
import com.nikamilon.database.domain.entity.AuthorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorRepositoryIntegrationTest {

    private AuthorRepositories underTest;

    @Autowired
    public void testThatAuthorCanBeCreatedAndRecalled(AuthorRepositories underTest){
        this.underTest = underTest;
    }

    @Test
    public void testThatAuthorCanBeCreatedAndRecalled(){
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        underTest.save(authorEntity);
        Optional<AuthorEntity> result = underTest.findById(authorEntity.getId());

        assertThat(result)
                .isPresent();
        assertThat(result.get())
                .isEqualTo(authorEntity);
    }

    @Test
    public void testThatMultipleAuthorsCanBeCreatedAndRecalled(){
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        underTest.save(authorEntityA);
        AuthorEntity authorEntityB = TestDataUtil.createTestAuthorB();
        underTest.save(authorEntityB);
        AuthorEntity authorEntityC = TestDataUtil.createTestAuthorC();
        underTest.save(authorEntityC);
        Iterable<AuthorEntity> result = underTest.findAll();

        assertThat(result)
                .hasSize(3)
                .containsExactly(authorEntityA, authorEntityB, authorEntityC);
    }

    @Test
    public void testThatAuthorCanBeUpdated(){
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        underTest.save(authorEntityA);
        authorEntityA.setName("UPDATED");
        underTest.save(authorEntityA);
        Optional<AuthorEntity> result = underTest.findById(authorEntityA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorEntityA);
    }

    @Test
    public void testThatAuthorCanBeDeleted(){
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        underTest.save(authorEntity);

        underTest.deleteById(authorEntity.getId());
        Optional<AuthorEntity> result = underTest.findById(authorEntity.getId());
        assertThat(result).isEmpty();
    }

    @Test
    public void testThatGetAuthorsWithAgeLessThan(){
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        underTest.save(authorEntityA);
        AuthorEntity authorEntityB = TestDataUtil.createTestAuthorB();
        underTest.save(authorEntityB);
        AuthorEntity authorEntityC = TestDataUtil.createTestAuthorC();
        underTest.save(authorEntityC);

        Iterable<AuthorEntity> result = underTest.ageLessThan(50);
        assertThat(result).containsExactly(authorEntityB, authorEntityC);
    }

    @Test
    public void testThatGetAuthorsWithAgeGreaterThan(){
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        underTest.save(authorEntityA);
        AuthorEntity authorEntityB = TestDataUtil.createTestAuthorB();
        underTest.save(authorEntityB);
        AuthorEntity authorEntityC = TestDataUtil.createTestAuthorC();
        underTest.save(authorEntityC);

        Iterable<AuthorEntity> result = underTest.findAuthorWithAgeGreaterThan(50);
        assertThat(result).containsExactly(authorEntityA);
    }
}
