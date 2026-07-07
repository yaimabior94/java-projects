package com.smartinventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartinventory.entity.Category;
import com.smartinventory.service.CategoryService;
import com.smartinventory.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller tests for CategoryController using @WebMvcTest.
 * Security auto-configuration is excluded so that we don't get 401/403 errors during MVC isolation testing.
 */
@WebMvcTest(controllers = CategoryController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
@DisplayName("Category Controller Tests")
@SuppressWarnings("null")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private com.smartinventory.security.JwtService jwtService;

    @MockBean
    private com.smartinventory.security.UserDetailsServiceImpl userDetailsService;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category(1L, "Electronics", "Electronic products");
    }

    @Test
    @DisplayName("GET /api/categories - Should return all categories")
    void testGetAllCategories() throws Exception {
        // Given
        List<Category> categories = Arrays.asList(
                new Category(1L, "Electronics", "Electronics"),
                new Category(2L, "Furniture", "Furniture")
        );
        when(categoryService.getAllCategories()).thenReturn(categories);

        // When & Then
        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", equalTo("Electronics")))
                .andExpect(jsonPath("$[1].name", equalTo("Furniture")));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    @DisplayName("GET /api/categories/{id} - Should return category")
    void testGetCategoryById_Success() throws Exception {
        // Given
        when(categoryService.getCategoryById(1L)).thenReturn(testCategory);

        // When & Then
        mockMvc.perform(get("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo("Electronics")));

        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    @DisplayName("GET /api/categories/{id} - Should return 404 when not found")
    void testGetCategoryById_NotFound() throws Exception {
        // Given
        when(categoryService.getCategoryById(999L))
                .thenThrow(new ResourceNotFoundException("Category not found"));

        // When & Then
        mockMvc.perform(get("/api/categories/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(categoryService, times(1)).getCategoryById(999L);
    }

    @Test
    @DisplayName("POST /api/categories - Should create category")
    void testCreateCategory_Success() throws Exception {
        // Given
        Category newCategory = new Category("New Category", "New Description");
        Category saved = new Category(1L, "New Category", "New Description");
        when(categoryService.createCategory(any(Category.class))).thenReturn(saved);

        // When & Then
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo("New Category")));

        verify(categoryService, times(1)).createCategory(any(Category.class));
    }

    @Test
    @DisplayName("PUT /api/categories/{id} - Should update category")
    void testUpdateCategory_Success() throws Exception {
        // Given
        Category updated = new Category(1L, "Updated", "Updated Description");
        when(categoryService.updateCategory(eq(1L), any(Category.class))).thenReturn(updated);

        // When & Then
        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Updated")));

        verify(categoryService, times(1)).updateCategory(eq(1L), any(Category.class));
    }

    @Test
    @DisplayName("DELETE /api/categories/{id} - Should delete category")
    void testDeleteCategory_Success() throws Exception {
        // Given
        doNothing().when(categoryService).deleteCategory(1L);

        // When & Then
        mockMvc.perform(delete("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteCategory(1L);
    }
}
