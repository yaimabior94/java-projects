# Testing Documentation

## Overview
This document explains the comprehensive test suite for the Smart Inventory Management System backend using JUnit 5, Mockito, and Spring Test.

## Test Layers

### 1. Repository Tests (@DataJpaTest)
**Purpose**: Test data persistence and custom JPA queries
**Framework**: Spring Data JPA + H2 in-memory database
**Files**:
- `ProductRepositoryTest.java` - 11 test cases
- `CategoryRepositoryTest.java` - 6 test cases

**What Gets Tested**:
- Save and retrieve entities
- Custom query methods (findBySku, findByNameContaining, etc.)
- Update and delete operations
- Relationship persistence
- Timestamps (@PrePersist, @PreUpdate)

**Example Test**:
```java
@Test
void testFindBySku() {
    // Given - Product with unique SKU
    Product product = createProduct("SKU-12345", "Product A", category);
    productRepository.save(product);

    // When - Find by SKU
    Optional<Product> found = productRepository.findBySku("SKU-12345");

    // Then - Should find the product
    assertThat(found).isPresent();
}
```

**Benefits**:
- Fast execution (H2 in-memory)
- No application context loading
- Database integration without HTTP layer
- Tests transaction behavior

### 2. Service Tests (@ExtendWith(MockitoExtension.class))
**Purpose**: Test business logic in isolation using mocked repositories
**Framework**: Mockito + AssertJ
**Files**:
- `ProductServiceTest.java` - 14 test cases
- `CategoryServiceTest.java` - 7 test cases

**What Gets Tested**:
- CRUD operations (Create, Read, Update, Delete)
- Search and filter operations
- Exception handling (ResourceNotFoundException)
- Transaction boundaries (@Transactional annotations)
- Mock verification (verify repository was called correctly)

**Example Test**:
```java
@Test
void testGetProductById_Success() {
    // Given - Mock repository returns a product
    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

    // When - Call service method
    Product result = productService.getProductById(1L);

    // Then - Verify result and interaction
    assertThat(result).isNotNull();
    verify(productRepository, times(1)).findById(1L);
}
```

**Benefits**:
- Very fast (no database)
- Tests business logic separately from persistence
- Easy to test error scenarios
- Can verify exact mock interactions
- Mock verification prevents missing calls

### 3. Controller Tests (@WebMvcTest)
**Purpose**: Test HTTP endpoints and request/response handling
**Framework**: Spring Test MockMvc + Jackson
**Files**:
- `ProductControllerTest.java` - 11 test cases
- `CategoryControllerTest.java` - 8 test cases

**What Gets Tested**:
- HTTP status codes (200, 201, 204, 404)
- Request/response JSON serialization
- Request validation (@Valid)
- Path variables and request parameters
- Content-Type headers
- Exception handling (404 Not Found, 400 Bad Request)

**Example Test**:
```java
@Test
void testCreateProduct_Success() throws Exception {
    // Given - Service returns created product
    when(productService.createProduct(any(Product.class)))
        .thenReturn(savedProduct);

    // When & Then - Perform POST and verify 201 CREATED
    mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(newProduct)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", equalTo(1)));
}
```

**Benefits**:
- Tests HTTP layer without full app context
- Fast execution
- Verifies correct status codes
- Tests input validation
- Verifies JSON structure

### 4. Integration Tests (@SpringBootTest)
**Purpose**: End-to-end testing with full application context
**Framework**: Spring Boot Test + H2 database + MockMvc
**Files**:
- `ProductIntegrationTest.java` - 15 test cases

**What Gets Tested**:
- Full request-response cycle (HTTP → Controller → Service → Repository → Database)
- Database transactions and rollback
- Spring configuration
- Bean injection
- Relationship integrity
- Data persistence across layers
- Real database operations

**Example Test**:
```java
@Test
void testFullCrudLifecycle() {
    // Create
    Product created = productService.createProduct(product);
    
    // Read
    Product retrieved = productService.getProductById(created.getId());
    
    // Update
    Product updated = productService.updateProduct(retrieved.getId(), retrieved);
    
    // Delete
    productService.deleteProduct(updated.getId());
    
    // Verify all in actual database
    assertThat(productRepository.findById(updated.getId())).isEmpty();
}
```

**Benefits**:
- Tests real database with H2
- Verifies configuration and wiring
- Tests actual transaction behavior
- Catches integration issues
- Tests full flow end-to-end

## Test Statistics

```
Repository Tests:     17 test cases
Service Tests:        21 test cases
Controller Tests:     19 test cases
Integration Tests:    15 test cases
─────────────────────────────────
Total:               72 test cases

Estimated Coverage:  > 80% code coverage
```

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=ProductServiceTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=ProductServiceTest#testGetProductById_Success
```

### Run With Coverage Report
```bash
mvn clean test jacoco:report
```
Coverage report: `target/site/jacoco/index.html`

### Run Tests by Layer
```bash
# Repository tests only
mvn test -Dtest=*RepositoryTest

# Service tests only
mvn test -Dtest=*ServiceTest

# Controller tests only
mvn test -Dtest=*ControllerTest

# Integration tests only
mvn test -Dtest=*IntegrationTest
```

## Test Naming Convention

All test classes follow JUnit naming conventions:
- Class: `[ClassName]Test.java`
- Methods: `test[MethodName]_[Scenario]`
- Examples:
  - `testGetProductById_Success`
  - `testCreateProduct_ValidationError`
  - `testDeleteProduct_NotFound`

## Assertion Libraries

### AssertJ (Fluent assertions)
```java
assertThat(product).isNotNull();
assertThat(products).hasSize(2);
assertThat(products).extracting("name").contains("Product 1");
assertThat(products).allMatch(p -> p.getIsActive());
```

### Hamcrest (Matcher-based)
```java
assertThat(result, notNullValue());
assertThat(products, hasSize(2));
assertThat(names, hasItems("Product 1", "Product 2"));
```

## Mock Verification Patterns

```java
// Verify called once
verify(repository, times(1)).findById(1L);

// Verify never called
verify(repository, never()).delete(any());

// Verify called multiple times
verify(repository, times(3)).save(any());

// Verify call order
InOrder inOrder = inOrder(repo1, repo2);
inOrder.verify(repo1).findById(1L);
inOrder.verify(repo2).save(any());
```

## Exception Testing

```java
// Using AssertJ
assertThatThrownBy(() -> service.getById(999))
    .isInstanceOf(ResourceNotFoundException.class)
    .hasMessageContaining("not found");

// Using JUnit
assertThrows(ResourceNotFoundException.class, 
    () -> service.getById(999));
```

## Common Test Patterns

### 1. Happy Path (Success Case)
```java
@Test
void testMethodName_Success() {
    // Given - setup mocks/data
    when(mock.method()).thenReturn(expected);
    
    // When - call method
    Result result = service.method();
    
    // Then - verify result
    assertThat(result).isEqualTo(expected);
}
```

### 2. Error Scenario
```java
@Test
void testMethodName_Error() {
    // Given - mock throws exception
    when(mock.method()).thenThrow(new SomeException());
    
    // When & Then - verify exception
    assertThatThrownBy(() -> service.method())
        .isInstanceOf(SomeException.class);
}
```

### 3. State Verification (Integration)
```java
@Test
void testMethodName_StateChange() {
    // Given - initial state
    assertThat(repository.count()).isEqualTo(0);
    
    // When - perform action
    service.createEntity(entity);
    
    // Then - verify state changed
    assertThat(repository.count()).isEqualTo(1);
}
```

## Dependencies Added to pom.xml

```xml
<!-- JUnit 5 (Included in spring-boot-starter-test) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- Mockito -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.5.1</version>
    <scope>test</scope>
</dependency>

<!-- Spring Security Test -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- H2 Database (for testing) -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

## Test Configuration (application-test.properties)

```properties
# H2 in-memory database
spring.datasource.url=jdbc:h2:mem:test
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.com.smartinventory=DEBUG
logging.level.org.springframework.web=INFO
```

## Best Practices

### 1. Arrange-Act-Assert (AAA) Pattern
```java
@Test
void testMethod() {
    // Arrange - Setup test data
    Product product = createProduct("SKU-001", "Product");
    
    // Act - Perform the action
    Product result = service.createProduct(product);
    
    // Assert - Verify the results
    assertThat(result.getId()).isNotNull();
}
```

### 2. Test One Thing Per Test
- Each test should verify only one behavior
- Easier to identify failures
- Better readability

### 3. Use Descriptive Names
- `testGetProductById_Success` ✓
- `testProduct()` ✗

### 4. Avoid Test Dependencies
- Tests should be independent
- Use @BeforeEach for setup
- Don't rely on test execution order

### 5. Mock External Dependencies
- Mock repository in service tests
- Mock service in controller tests
- Use real database only in integration tests

### 6. Test Edge Cases
- Null values
- Empty collections
- Invalid input
- Maximum/minimum values

## Continuous Integration

These tests are designed to run in CI/CD pipelines:
- Fast execution (< 10 seconds)
- No external dependencies
- Deterministic (no flakiness)
- Clear failure messages

## Coverage Goals

- **Controller Layer**: 80%+ coverage
- **Service Layer**: 85%+ coverage
- **Repository Layer**: 90%+ coverage
- **Overall**: 80%+ coverage

## Next Steps

To extend the test suite to other entities:
1. Create `[Entity]RepositoryTest.java` following ProductRepositoryTest pattern
2. Create `[Entity]ServiceTest.java` following ProductServiceTest pattern
3. Create `[Entity]ControllerTest.java` following ProductControllerTest pattern
4. Update ProductIntegrationTest with new entity scenarios

## Troubleshooting

### H2 Database Issues
- Ensure `spring.jpa.hibernate.ddl-auto=create-drop` in application-test.properties
- Check schema creation from entity annotations

### Mock Not Working
- Verify @ExtendWith(MockitoExtension.class) is present
- Ensure @Mock and @InjectMocks annotations are used correctly

### MockMvc 404 Errors
- Verify @WebMvcTest includes correct controller class
- Check request path matches controller @RequestMapping
- Verify controller methods have correct HTTP verb

### Transaction Issues
- Use @Transactional on integration tests
- Verify entityManager is flushed for state verification
- Check rollback behavior in test properties
