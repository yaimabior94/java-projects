package com.smartinventory.repository;

import com.smartinventory.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Repository tests for Category entity using @DataJpaTest
 *
 * Pattern: Similar to ProductRepositoryTest
 * - Tests JPA query methods
 * - Tests persistence operations
 * - Uses H2 in-memory database
 */
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("Category Repository Tests")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        // Clear before each test
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save and retrieve category by id")
    void testSaveAndFindById() {
        // Given
        Category category = new Category("Electronics", "Electronic products");

        // When
        Category saved = categoryRepository.save(category);

        // Then
        Optional<Category> retrieved = categoryRepository.findById(saved.getId());
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getName()).isEqualTo("Electronics");
    }

    @Test
    @DisplayName("Should update category")
    void testUpdateCategory() {
        // Given
        Category category = categoryRepository.save(new Category("Original", "Original Description"));
        Long categoryId = category.getId();

        // When
        category.setName("Updated");
        categoryRepository.save(category);

        // Then
        Category updated = categoryRepository.findById(categoryId).get();
        assertThat(updated.getName()).isEqualTo("Updated");
    }

    @Test
    @DisplayName("Should delete category")
    void testDeleteCategory() {
        // Given
        Category category = categoryRepository.save(new Category("To Delete", "To Delete"));
        Long categoryId = category.getId();

        // When
        categoryRepository.deleteById(categoryId);

        // Then
        assertThat(categoryRepository.findById(categoryId)).isEmpty();
    }

    @Test
    @DisplayName("Should count total categories")
    void testCountCategories() {
        // Given
        categoryRepository.save(new Category("Cat 1", "Desc 1"));
        categoryRepository.save(new Category("Cat 2", "Desc 2"));
        categoryRepository.save(new Category("Cat 3", "Desc 3"));

        // When
        long count = categoryRepository.count();

        // Then
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("Should return empty when category not found")
    void testFindById_NotFound() {
        // When & Then
        assertThat(categoryRepository.findById(999L)).isEmpty();
    }
}
