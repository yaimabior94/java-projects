# Comprehensive Test Suite Explanation

## Test Overview

This document provides detailed explanations of each test type, including what's being tested, why it matters, and how each test contributes to overall coverage.

## 1. REPOSITORY TESTS (JPA/Data Layer)

### Purpose
Repository tests verify that data persistence works correctly without involving business logic or HTTP layer.

### Test Framework
- **@DataJpaTest**: Only loads JPA configuration, not full app context
- **TestEntityManager**: Manages entities during tests
- **H2 In-Memory Database**: Provides isolated database for each test
- **Transactions**: Automatically rolled back after each test

### ProductRepositoryTest - 11 Tests

#### Test 1: testSaveAndFindById
```
What: Save product and retrieve by ID
Why: Verifies basic persistence works
How: 
  1. Create Product object
  2. Save via repository
  3. Retrieve by ID
  4. Assert all fields match
```

#### Test 2: testFindBySku
```
What: Find product by unique SKU
Why: Tests custom @Query method
How:
  1. Save product with specific SKU
  2. Query by SKU
  3. Verify returned product matches
```

#### Test 3: testFindBySku_NotFound
```
What: Return empty Optional when SKU doesn't exist
Why: Handles not-found scenario
How:
  1. Query non-existent SKU
  2. Verify returns empty Optional
```

#### Test 4: testFindByNameContainingIgnoreCase
```
What: Search products by partial name (case-insensitive)
Why: Tests query method with LIKE and LOWER operators
How:
  1. Create multiple products with similar names
  2. Search for partial text in lowercase
  3. Verify returns all matching products
```

#### Test 5: testFindByCategoryId
```
What: Find products by category relationship
Why: Tests foreign key queries
How:
  1. Create multiple categories and products
  2. Query by specific category ID
  3. Verify returns only products in that category
```

#### Test 6: testFindByIsActiveTrue
```
What: Find only active products
Why: Tests boolean field queries
How:
  1. Create mix of active/inactive products
  2. Query for active only
  3. Verify only active returned
```

#### Test 7: testFindByStockQuantityLessThanEqual
```
What: Find low stock products
Why: Tests numeric comparisons
How:
  1. Create products with varying stock levels
  2. Query with threshold
  3. Verify returns only below threshold
```

#### Test 8: testUpdateProduct
```
What: Update existing product fields
Why: Tests persistence of changes
How:
  1. Save product
  2. Modify fields
  3. Save again
  4. Retrieve and verify changes persisted
```

#### Test 9: testDeleteProduct
```
What: Delete product and verify removal
Why: Tests deletion and cascade behavior
How:
  1. Save product
  2. Delete by ID
  3. Verify no longer exists
```

#### Test 10: testCountAllProducts
```
What: Count total products
Why: Tests aggregation queries
How:
  1. Save multiple products
  2. Count all
  3. Verify count matches
```

#### Test 11: testFindByStockQuantityLessThan
```
What: Find products with stock strictly less than value
Why: Tests strict less-than queries
How:
  1. Create products with various stock
  2. Query with strict threshold
  3. Verify doesn't include boundary value
```

### Key Assertions in Repository Tests
- `assertThat(optional).isPresent()` - Verify entity found
- `assertThat(list).hasSize(n)` - Verify correct number of results
- `assertThat(list).extracting("field").contains(value)` - Verify specific fields
- `assertThat(list).allMatch(predicate)` - All items match condition

---

## 2. SERVICE TESTS (Business Logic Layer)

### Purpose
Service tests verify business logic works correctly without database or HTTP layer. Uses Mockito to mock repository.

### Test Framework
- **@ExtendWith(MockitoExtension.class)**: Enables Mockito annotations
- **@Mock**: Creates mock repository
- **@InjectMocks**: Injects mock into service
- **when/thenReturn**: Configures mock behavior
- **verify**: Confirms mock was called correctly

### ProductServiceTest - 14 Tests

#### Test 1: testGetAllProducts
```
What: Retrieve all products from repository
Why: Tests service forwards repository calls
How:
  1. Mock repository to return 2 products
  2. Call service.getAllProducts()
  3. Assert returns list with 2 products
  4. Verify repository was called exactly once
```

#### Test 2: testGetAllProducts_Empty
```
What: Handle empty product list
Why: Tests service with no data
How:
  1. Mock repository to return empty list
  2. Call service method
  3. Assert returns empty
  4. Verify repository called
```

#### Test 3: testGetProductById_Success
```
What: Retrieve single product by ID
Why: Tests successful retrieval
How:
  1. Mock repository to return product
  2. Call service.getProductById(1)
  3. Assert product details correct
  4. Verify called with correct ID
```

#### Test 4: testGetProductById_NotFound
```
What: Throw exception when product doesn't exist
Why: Tests error handling
How:
  1. Mock repository to return empty
  2. Call service
  3. Assert throws ResourceNotFoundException
  4. Verify exception has correct message
```

#### Test 5: testCreateProduct_Success
```
What: Create new product
Why: Tests save operation
How:
  1. Create new product (no ID)
  2. Mock repository returns saved product with ID
  3. Call service.createProduct()
  4. Assert ID is assigned
  5. Verify save called once
```

#### Test 6: testUpdateProduct_Success
```
What: Update existing product
Why: Tests modification logic
How:
  1. Mock repository to return existing product
  2. Create updated data
  3. Call service.updateProduct()
  4. Assert all fields updated
  5. Verify save called with updated values
```

#### Test 7: testUpdateProduct_NotFound
```
What: Throw exception when updating non-existent product
Why: Tests error handling on update
How:
  1. Mock repository returns empty for ID
  2. Call service to update
  3. Assert throws ResourceNotFoundException
  4. Verify save never called (no orphan update)
```

#### Test 8: testDeleteProduct_Success
```
What: Delete product by ID
Why: Tests deletion flow
How:
  1. Mock repository to return product
  2. Call service.deleteProduct()
  3. Verify delete called with correct product
  4. Verify findById called first
```

#### Test 9: testDeleteProduct_NotFound
```
What: Throw exception when deleting non-existent product
Why: Tests error handling on delete
How:
  1. Mock repository returns empty
  2. Call service.deleteProduct()
  3. Assert throws ResourceNotFoundException
  4. Verify delete never called
```

#### Test 10: testFindBySku_Success
```
What: Find product by SKU
Why: Tests specific query
How:
  1. Mock findBySku to return product
  2. Call service.findBySku()
  3. Assert returns correct product
  4. Verify called with correct SKU
```

#### Test 11: testFindBySku_NotFound
```
What: Return empty when SKU not found
Why: Tests query with no results
How:
  1. Mock findBySku to return empty
  2. Call service
  3. Assert returns empty Optional
  4. Verify correct SKU searched
```

#### Test 12: testSearchProductsByName
```
What: Search products by name
Why: Tests search functionality
How:
  1. Mock findByNameContaining to return matching products
  2. Call service.searchProductsByName()
  3. Assert returns correct results
  4. Verify all results contain search term
```

#### Test 13: testSearchProductsByName_NoResults
```
What: Return empty when search finds nothing
Why: Tests no-match scenario
How:
  1. Mock search to return empty
  2. Call service
  3. Assert returns empty list
  4. Verify search was attempted
```

#### Test 14: testGetLowStockProducts
```
What: Find products below stock threshold
Why: Tests inventory alert query
How:
  1. Mock to return products with stock <= threshold
  2. Call service.getLowStockProducts(10)
  3. Assert all have stock <= 10
  4. Verify correct threshold used
```

### Key Mockito Patterns
```java
// Configure mock to return value
when(repository.findById(1L)).thenReturn(Optional.of(product));

// Configure mock to throw exception
when(repository.findById(999L)).thenThrow(new NotFoundException());

// Verify called once with specific argument
verify(repository, times(1)).findById(1L);

// Verify never called
verify(repository, never()).delete(any());

// Capture arguments passed to mock
ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
verify(repository).save(captor.capture());
Product saved = captor.getValue();
```

---

## 3. CONTROLLER TESTS (HTTP/REST API Layer)

### Purpose
Controller tests verify HTTP endpoints work correctly without database. Uses MockMvc to simulate HTTP requests.

### Test Framework
- **@WebMvcTest**: Loads only web layer
- **MockMvc**: Simulates HTTP requests and verifies responses
- **ObjectMapper**: Serializes Java objects to JSON
- **status()**: Verifies HTTP status codes
- **jsonPath()**: Extracts and asserts JSON fields

### ProductControllerTest - 11 Tests

#### Test 1: testGetAllProducts
```
What: GET /api/products returns all products with 200 OK
Why: Tests successful list endpoint
How:
  1. Mock service to return 2 products
  2. Send GET request
  3. Assert status 200
  4. Assert response body contains 2 products
  5. Verify service called
```

#### Test 2: testGetAllProducts_Empty
```
What: GET /api/products returns empty array
Why: Tests list endpoint with no data
How:
  1. Mock service to return empty list
  2. Send GET request
  3. Assert status 200
  4. Assert response is empty array
```

#### Test 3: testGetProductById_Success
```
What: GET /api/products/{id} returns single product
Why: Tests get-by-id endpoint
How:
  1. Mock service to return product
  2. Send GET /api/products/1
  3. Assert status 200
  4. Assert all fields in response match
  5. Verify path variable passed correctly
```

#### Test 4: testGetProductById_NotFound
```
What: GET /api/products/{id} returns 404 when not found
Why: Tests error response
How:
  1. Mock service to throw ResourceNotFoundException
  2. Send GET request
  3. Assert status 404
  4. Verify exception thrown in service
```

#### Test 5: testSearchProducts
```
What: GET /api/products/search?name=query returns matches
Why: Tests search endpoint
How:
  1. Mock service to return matching products
  2. Send GET with query parameter
  3. Assert status 200
  4. Assert response contains only matches
  5. Verify query param passed to service
```

#### Test 6: testCreateProduct_Success
```
What: POST /api/products creates product with 201 CREATED
Why: Tests create endpoint
How:
  1. Mock service to return created product with ID
  2. Send POST with JSON body
  3. Assert status 201
  4. Assert ID returned in response
  5. Verify service called with correct data
```

#### Test 7: testCreateProduct_ValidationError
```
What: POST /api/products returns 400 when data invalid
Why: Tests request validation
How:
  1. Send POST with missing required fields
  2. Assert status 400 Bad Request
  3. Verify service never called (validation before service)
```

#### Test 8: testUpdateProduct_Success
```
What: PUT /api/products/{id} updates and returns 200
Why: Tests update endpoint
How:
  1. Mock service to return updated product
  2. Send PUT with ID and JSON body
  3. Assert status 200
  4. Assert updated fields in response
  5. Verify service called with correct ID and data
```

#### Test 9: testUpdateProduct_NotFound
```
What: PUT /api/products/{id} returns 404 if not found
Why: Tests update error handling
How:
  1. Mock service to throw NotFoundException
  2. Send PUT request
  3. Assert status 404
```

#### Test 10: testDeleteProduct_Success
```
What: DELETE /api/products/{id} returns 204 NO CONTENT
Why: Tests delete endpoint
How:
  1. Mock service to complete successfully
  2. Send DELETE request
  3. Assert status 204 (No Content)
  4. Verify service called with correct ID
```

#### Test 11: testDeleteProduct_NotFound
```
What: DELETE /api/products/{id} returns 404 if not found
Why: Tests delete error handling
How:
  1. Mock service to throw NotFoundException
  2. Send DELETE request
  3. Assert status 404
```

### Key MockMvc Assertions
```java
// Verify HTTP status
.andExpect(status().isOk())           // 200
.andExpect(status().isCreated())      // 201
.andExpect(status().isNoContent())    // 204
.andExpect(status().isNotFound())     // 404
.andExpect(status().isBadRequest())   // 400

// Verify JSON content
.andExpect(jsonPath("$.id", equalTo(1)))
.andExpect(jsonPath("$", hasSize(2)))
.andExpect(jsonPath("$[*].name", hasItems("A", "B")))

// Verify headers
.andExpect(content().contentType(MediaType.APPLICATION_JSON))
```

---

## 4. INTEGRATION TESTS (End-to-End)

### Purpose
Integration tests verify complete flow from HTTP request through all layers to database.

### Test Framework
- **@SpringBootTest**: Loads complete application context
- **@AutoConfigureMockMvc**: Configures MockMvc for integration testing
- **H2 Real Database**: Uses actual database (in-memory)
- **Real Services**: Uses actual service implementations
- **Real Controllers**: Tests actual controllers

### ProductIntegrationTest - 15 Tests

#### Test 1: testCreateProductThroughService
```
What: Create product via service and verify in database
Why: Tests service-repository integration
How:
  1. Create product object
  2. Save via service
  3. Query database directly
  4. Assert product exists with correct ID
```

#### Test 2: testFullCrudLifecycle
```
What: Complete CRUD: Create → Read → Update → Delete
Why: Tests all operations work together
How:
  1. Create product → Assert ID assigned
  2. Retrieve product → Assert fields match
  3. Update product → Assert changes persisted
  4. Delete product → Assert removed from database
```

#### Test 3: testSearchWithMultipleProducts
```
What: Search multiple products with different criteria
Why: Tests search with real data
How:
  1. Create 3 products with similar names
  2. Search for partial match
  3. Assert returns correct subset
  4. Verify case-insensitive search works
```

#### Test 4: testLowStockProductsIntegration
```
What: Find low stock products from actual database
Why: Tests inventory threshold query
How:
  1. Create products with various stock levels
  2. Query for stock <= threshold
  3. Assert only matching products returned
  4. Verify database query executed correctly
```

#### Test 5: testCreateProductViaHttp
```
What: Send HTTP POST and verify in database
Why: Tests full HTTP to database flow
How:
  1. Send POST request with JSON
  2. Verify 201 response with ID
  3. Query database for product
  4. Assert product persisted correctly
```

#### Test 6: testGetProductsViaHttp
```
What: Send HTTP GET and verify data from database
Why: Tests retrieval from actual database
How:
  1. Create products in database
  2. Send GET request
  3. Verify response contains all products
  4. Assert count matches database
```

#### Test 7: testUpdateProductViaHttp
```
What: Send HTTP PUT and verify database updated
Why: Tests update flow end-to-end
How:
  1. Create product
  2. Send PUT with updated data
  3. Query database
  4. Assert changes persisted
```

#### Test 8: testDeleteProductViaHttp
```
What: Send HTTP DELETE and verify removal from database
Why: Tests deletion end-to-end
How:
  1. Create product
  2. Send DELETE request
  3. Query database
  4. Assert product gone
```

#### Test 9: testSearchViaHttp
```
What: Send search request and get results from database
Why: Tests search via HTTP layer
How:
  1. Create multiple products
  2. Send GET with search parameter
  3. Verify correct subset returned
  4. Assert query executed in database
```

#### Test 10: testTransactionRollback
```
What: Transaction behavior with exceptions
Why: Tests data consistency
How:
  1. Create product
  2. Trigger error (attempt invalid operation)
  3. Assert original product still exists
  4. Verify transaction isolation
```

#### Test 11: testProductTimestamps
```
What: Verify createdAt and updatedAt are set
Why: Tests @PrePersist and @PreUpdate
How:
  1. Create product
  2. Save via service
  3. Query from database
  4. Assert both timestamps set
  5. Assert updatedAt >= createdAt
```

#### Test 12: testProductCategoryAssociation
```
What: Product-Category relationship persists
Why: Tests foreign key relationship
How:
  1. Create product with category
  2. Save to database
  3. Retrieve product
  4. Assert category loaded correctly
```

#### Test 13: testHttpGetById
```
What: GET specific product by ID via HTTP
Why: Tests path variable handling
How:
  1. Create product
  2. Send GET /api/products/{id}
  3. Assert 200 with product data
```

#### Test 14: testHttpPutUpdate
```
What: Update via HTTP with specific ID
Why: Tests HTTP update routing
How:
  1. Create product
  2. Send PUT /api/products/{id}
  3. Verify update successful
```

#### Test 15: testHttpDelete
```
What: Delete via HTTP with specific ID
Why: Tests HTTP delete routing
How:
  1. Create product
  2. Send DELETE /api/products/{id}
  3. Verify 204 No Content
  4. Assert deleted from database
```

---

## Test Coverage Breakdown

### Repository Layer Coverage
- Save/Insert: ✓
- Retrieve (by ID): ✓
- Custom queries (findBySku, findByName, etc.): ✓
- Update: ✓
- Delete: ✓
- Aggregations (count): ✓
- Relationships (joins): ✓

### Service Layer Coverage
- CRUD operations: ✓
- Business logic: ✓
- Exception handling: ✓
- Repository interaction: ✓
- Mock verification: ✓

### Controller Layer Coverage
- HTTP methods (GET, POST, PUT, DELETE): ✓
- Status codes (200, 201, 204, 404): ✓
- Request/response JSON: ✓
- Path variables: ✓
- Query parameters: ✓
- Request validation: ✓
- Error responses: ✓

### Integration Coverage
- Full request-response cycle: ✓
- Multiple layer interaction: ✓
- Database transactions: ✓
- Data persistence: ✓
- Relationships: ✓
- Timestamps: ✓

---

## Test Execution Flow

```
1. REPOSITORY TESTS (10-15 ms each)
   ├─ @DataJpaTest loads only JPA config
   ├─ H2 creates schema
   ├─ Tests run with DB operations
   └─ Transaction rolls back

2. SERVICE TESTS (1-5 ms each)
   ├─ @ExtendWith(MockitoExtension) loads Mockito
   ├─ Mocks created for dependencies
   ├─ Tests run with mocked repository
   └─ No database access

3. CONTROLLER TESTS (5-10 ms each)
   ├─ @WebMvcTest loads only web layer
   ├─ MockMvc simulates HTTP
   ├─ Service mocked
   └─ Tests verify HTTP layer

4. INTEGRATION TESTS (50-100 ms each)
   ├─ @SpringBootTest loads full context
   ├─ H2 creates real database
   ├─ Tests run end-to-end
   └─ Tests verify integration

Total Test Suite: ~200ms for 72 tests
```

---

## Common Test Issues & Solutions

| Issue | Cause | Solution |
|-------|-------|----------|
| NullPointerException in service test | Repository mock not configured | Add `when(mock.method()).thenReturn(value)` |
| 404 in controller test | Service not mocked | Add `@MockBean private Service service;` |
| H2 schema error | Hibernate DDL-auto wrong | Set `spring.jpa.hibernate.ddl-auto=create-drop` |
| Test transaction not rolling back | Missing @Transactional | Add `@Transactional` to test method |
| Assertion fails intermittently | Test dependencies | Remove interdependencies, use @BeforeEach |

---

## How to Add Tests for New Entities

For each new entity (e.g., Supplier, Inventory):

1. Create `SupplierRepositoryTest.java`
   - Follow ProductRepositoryTest pattern
   - Test custom queries
   - Test relationships

2. Create `SupplierServiceTest.java`
   - Follow ProductServiceTest pattern
   - Mock repository
   - Test business logic

3. Create `SupplierControllerTest.java`
   - Follow ProductControllerTest pattern
   - Test HTTP endpoints
   - Test status codes

4. Add to `ProductIntegrationTest.java`
   - Add integration test cases
   - Test entity-specific flows

Estimated time per entity: 30-45 minutes
