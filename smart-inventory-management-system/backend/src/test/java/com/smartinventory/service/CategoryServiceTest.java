package com.smartinventory.service;

import com.smartinventory.entity.Category;
import com.smartinventory.exception.ResourceNotFoundException;
import com.smartinventory.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Service layer tests for CategoryService using Mockito
 *
 * Pattern: Similar to ProductServiceTest
 * - Mock repository
 * - Test business logic in isolation
 * - Verify correct exception handling
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Category Service Tests")
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category(1L, "Electronics", "Electronic products");
    }

    @Test
    @DisplayName("Should retrieve all categories")
    void testGetAllCategories() {
        // Given
        List<Category> categories = Arrays.asList(
                new Category(1L, "Electronics", "Electronics"),
                new Category(2L, "Furniture", "Furniture")
        );
        when(categoryRepository.findAll()).thenReturn(categories);

        // When
        List<Category> result = categoryService.getAllCategories();

        // Then
        assertThat(result).hasSize(2);
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get category by id successfully")
    void testGetCategoryById_Success() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        // When
        Category result = categoryService.getCategoryById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Electronics");
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when category not found")
    void testGetCategoryById_NotFound() {
        // Given
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> categoryService.getCategoryById(999L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(categoryRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should create category successfully")
    void testCreateCategory_Success() {
        // Given
        Category newCategory = new Category("New Category", "New Description");
        Category saved = new Category(1L, "New Category", "New Description");
        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        // When
        Category result = categoryService.createCategory(newCategory);

        // Then
        assertThat(result.getId()).isEqualTo(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Should update category successfully")
    void testUpdateCategory_Success() {
        // Given
        Category updateData = new Category("Updated", "Updated Description");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        // When
        Category result = categoryService.updateCategory(1L, updateData);

        // Then
        assertThat(result).isNotNull();
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Should delete category successfully")
    void testDeleteCategory_Success() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        doNothing().when(categoryRepository).delete(testCategory);

        // When
        categoryService.deleteCategory(1L);

        // Then
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).delete(testCategory);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent category")
    void testDeleteCategory_NotFound() {
        // Given
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> categoryService.deleteCategory(999L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(categoryRepository, never()).delete(any());
    }
}
