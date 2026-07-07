# SMART INVENTORY MANAGEMENT SYSTEM

## Complete Project Documentation

**University Final Year Project Report**

---

# TABLE OF CONTENTS

1. [Introduction](#introduction)
2. [Objectives](#objectives)
3. [Problem Statement](#problem-statement)
4. [Scope](#scope)
5. [OOP Concepts](#oop-concepts)
6. [System Architecture](#system-architecture)
7. [ER Diagram](#er-diagram)
8. [Use Case Diagram](#use-case-diagram)
9. [Class Diagram](#class-diagram)
10. [Sequence Diagrams](#sequence-diagrams)
11. [API Documentation](#api-documentation)
12. [Screenshots](#screenshots)
13. [Conclusion](#conclusion)
14. [Future Improvements](#future-improvements)

---

# INTRODUCTION

## Project Title
**Smart Inventory Management System**

## Description
The Smart Inventory Management System is a comprehensive web-based application designed to streamline and optimize inventory operations for businesses of all sizes. The system provides real-time inventory tracking, automated stock level monitoring, intelligent purchasing recommendations, and detailed analytics reports.

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.3.1
- **Language**: Java 17
- **Build Tool**: Maven
- **Database**: MySQL 8.4
- **API**: RESTful Web Services
- **Authentication**: JWT (JSON Web Tokens)
- **Testing**: JUnit 5, Mockito, Spring Test

### Frontend
- **Framework**: React 18.3.1
- **Build Tool**: Vite 5.3.1
- **HTTP Client**: Axios 1.7.2
- **Data Visualization**: Chart.js 4.4.3
- **Styling**: CSS3 with Dark Theme
- **Routing**: React Router 6.24.1

### Development Environment
- **IDE**: Visual Studio Code
- **Version Control**: Git
- **Architecture**: Microservices-ready (Layered Architecture)

## Project Team
- **Department**: Computer Science/Information Technology
- **Academic Year**: 2025-2026
- **Project Duration**: 6 months

---

# OBJECTIVES

## Primary Objectives

### 1. Inventory Optimization
- Minimize stock-out situations and prevent excess inventory
- Reduce carrying costs through intelligent inventory management
- Implement automated low-stock alert system
- Provide real-time stock level visibility

### 2. Operational Efficiency
- Automate inventory tracking and updates
- Reduce manual data entry and associated errors
- Streamline purchase order creation and tracking
- Improve warehouse organization

### 3. Business Analytics
- Generate comprehensive inventory reports
- Track sales trends and patterns
- Monitor supplier performance
- Calculate key inventory metrics (stock turnover, holding costs)

### 4. User Experience
- Provide intuitive user interface
- Enable multi-user access with role-based permissions
- Support various user types (Admin, Manager, Staff, Viewer)
- Implement real-time notifications for critical events

### 5. System Reliability
- Ensure data security and integrity
- Implement comprehensive error handling
- Provide consistent performance
- Support scalability for future growth

## Secondary Objectives

- Implement complete RESTful API documentation
- Achieve comprehensive test coverage (>80%)
- Provide detailed system documentation
- Enable easy deployment and maintenance
- Design for future feature extensibility

---

# PROBLEM STATEMENT

## Current Challenges

### Problem 1: Manual Inventory Management
**Issue**: Many businesses still rely on manual inventory tracking using spreadsheets or paper-based systems.

**Impact**:
- High error rates in stock counting
- Delayed information updates
- Difficulty in real-time decision making
- Time-consuming manual reconciliation

### Problem 2: Inefficient Stock Control
**Issue**: Lack of automated mechanisms to monitor stock levels and reorder points.

**Impact**:
- Frequent stock-outs leading to lost sales
- Overstocking resulting in excess inventory costs
- Inability to predict demand patterns
- Poor inventory turnover

### Problem 3: Fragmented Data Systems
**Issue**: Inventory data scattered across multiple systems without integration.

**Impact**:
- Data inconsistencies and conflicts
- Difficulty in generating comprehensive reports
- Poor visibility across departments
- Inefficient decision-making based on incomplete data

### Problem 4: Lack of Visibility
**Issue**: No real-time insights into inventory status, sales trends, or supplier performance.

**Impact**:
- Difficulty in tracking product movement
- Ineffective supplier management
- Limited business intelligence
- Poor strategic planning

### Problem 5: Limited Analytics
**Issue**: Absence of advanced analytics and reporting capabilities.

**Impact**:
- Inability to identify slow-moving products
- Difficulty in calculating inventory costs
- Limited insights into sales patterns
- Poor forecasting accuracy

## Solution Approach

The Smart Inventory Management System addresses these challenges through:

1. **Centralized Database**: Single source of truth for all inventory data
2. **Automated Tracking**: Real-time updates and automatic stock movements
3. **Intelligent Alerts**: Automated low-stock notifications and reorder recommendations
4. **Comprehensive Analytics**: Detailed reports and visual dashboards
5. **User-Friendly Interface**: Intuitive web-based application accessible to all users
6. **Role-Based Access**: Secure multi-user system with appropriate permissions
7. **RESTful API**: Modern API for future integrations

---

# SCOPE

## What is Included

### Functional Requirements

#### 1. Product Management
- Create, read, update, delete (CRUD) products
- Manage product categories
- Track product suppliers
- Set reorder levels and reorder quantities
- Manage product SKU (Stock Keeping Unit)
- Activate/deactivate products

#### 2. Inventory Tracking
- Real-time stock quantity updates
- Automatic inventory adjustments from sales and purchases
- Low-stock alerts and notifications
- Inventory history tracking
- Stock movement logs
- Multi-location inventory (future-ready)

#### 3. Sales Management
- Record sales transactions
- Manage individual sale items
- Track sales by period (daily, weekly, monthly)
- Calculate revenue metrics
- Update inventory on sales
- Generate sales reports

#### 4. Purchase Management
- Create purchase orders
- Track purchase receipts
- Manage purchase items
- Update inventory from purchases
- Record supplier information
- Track purchase history

#### 5. Supplier Management
- Maintain supplier database
- Track supplier contact information
- Monitor supplier performance
- Manage supplier categories
- Track payment terms
- Historical transaction records

#### 6. Analytics & Reporting
- Inventory reports (current stock, stock value)
- Sales reports (total sales, revenue, trends)
- Purchase reports (total purchases, costs)
- Low-stock product lists
- Monthly revenue summaries
- Category-wise sales analysis
- Supplier performance metrics

#### 7. Dashboard & Visualization
- Real-time dashboard with key metrics
- Sales charts (bar, line graphs)
- Inventory distribution (pie charts)
- Low-stock widget
- Monthly revenue cards
- Quick access to critical information

#### 8. User Management
- User registration and authentication
- Role-based access control (RBAC)
- User roles: Admin, Manager, Staff, Viewer
- User profile management
- Secure login with JWT tokens

#### 9. Authentication & Security
- User login/logout
- JWT token-based authentication
- Password security
- Session management
- Role-based endpoint authorization
- Request validation

### Non-Functional Requirements

#### 1. Performance
- Response time < 2 seconds for typical operations
- Support for at least 100 concurrent users
- Database query optimization
- Efficient API response times

#### 2. Scalability
- Modular architecture for easy extension
- Database schema designed for growth
- Stateless API for horizontal scaling
- Support for future microservices migration

#### 3. Security
- Data encryption in transit (HTTPS)
- Secure password storage (hashing)
- Input validation and sanitization
- SQL injection prevention
- CSRF protection
- Authorization checks on all endpoints

#### 4. Reliability
- 99% uptime target
- Graceful error handling
- Transaction management
- Data backup and recovery procedures
- Comprehensive logging

#### 5. Maintainability
- Clean, well-documented code
- Layered architecture (Controller → Service → Repository)
- Consistent naming conventions
- Comprehensive unit tests (>80% coverage)
- API documentation
- Deployment guides

#### 6. Usability
- Intuitive user interface
- Dark theme for reduced eye strain
- Responsive design for multiple devices
- Clear navigation
- Help tooltips and guidance
- Accessible color schemes

## What is Excluded

- Multi-language support (English only)
- Mobile native applications (Web-based only)
- Advanced machine learning for demand forecasting (Phase 2)
- Multi-company management (Single organization)
- Financial accounting module (Inventory-focused only)
- Barcode scanning (Manual entry only)
- Multi-warehouse management with transfers (Single location)
- EDI (Electronic Data Interchange) integrations
- Blockchain integration for supply chain

## Boundaries

### User Access
- System accessible to authorized users only
- Geographic location not restricted
- Internet access required
- Modern browser support (Chrome, Firefox, Edge, Safari)

### Data
- Inventory data stored centrally in MySQL database
- Historical data retained for audit purposes
- No third-party data sharing
- GDPR-compliant data handling

### Integration
- No pre-built integrations with external systems (Phase 1)
- Custom API for future integrations (ready for implementation)
- Standalone application (not part of larger ERP system)

---

# OOP CONCEPTS

## Object-Oriented Programming Implementation

The Smart Inventory Management System demonstrates comprehensive use of OOP principles throughout its architecture.

## 1. Encapsulation

### Definition
Bundling data (attributes) and methods (functions) that operate on that data within a single unit (class), while hiding internal details from the outside world.

### Implementation in Project

#### Product Entity
```java
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "sku", nullable = false, unique = true)
    private String sku;  // Private attribute
    
    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;  // Encapsulated
    
    @Column(name = "stock_quantity")
    private Integer stockQuantity;  // Encapsulated
    
    // Public getter/setter methods control access
    public Long getId() {
        return id;
    }
    
    public void setStockQuantity(Integer quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        this.stockQuantity = quantity;
    }
}
```

**Benefits**:
- Data validation in setters
- Hide internal implementation details
- Protect data integrity
- Control how data is accessed and modified

#### Service Layer Encapsulation
```java
@Service
public class ProductService {
    private final ProductRepository productRepository;
    
    // Constructor injection - dependency hidden from outside
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    // Public interface
    @Transactional
    public Product createProduct(Product product) {
        validateProduct(product);  // Private validation
        return productRepository.save(product);
    }
    
    // Private helper - hidden from outside
    private void validateProduct(Product product) {
        if (product.getSku() == null || product.getSku().isEmpty()) {
            throw new ValidationException("SKU is required");
        }
    }
}
```

## 2. Inheritance

### Definition
A mechanism where a new class is derived from an existing class, inheriting its properties and methods while allowing customization.

### Implementation in Project

#### Entity Base Class Pattern
```java
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

// All entities inherit common behavior
@Entity
public class Product extends BaseEntity {
    // Inherits id, createdAt, updatedAt, and lifecycle methods
    
    @Column(name = "sku")
    private String sku;
    
    // Product-specific attributes
}

@Entity
public class Category extends BaseEntity {
    // Inherits timestamp functionality
    
    @Column(name = "name")
    private String name;
}
```

#### Service Layer Inheritance
```java
public abstract class BaseService<T, ID> {
    protected final JpaRepository<T, ID> repository;
    
    public BaseService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }
    
    public List<T> getAll() {
        return repository.findAll();
    }
    
    public T getById(ID id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
    }
    
    public T save(T entity) {
        return repository.save(entity);
    }
}

@Service
public class ProductService extends BaseService<Product, Long> {
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        super(productRepository);
        this.productRepository = productRepository;
    }
    
    // Product-specific methods
    @Transactional(readOnly = true)
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
}

@Service
public class CategoryService extends BaseService<Category, Long> {
    private final CategoryRepository categoryRepository;
    
    public CategoryService(CategoryRepository categoryRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
    }
    
    // Category-specific methods
}
```

**Benefits**:
- Code reusability
- Consistent behavior across entities
- Reduced code duplication
- Easy maintenance and updates

## 3. Polymorphism

### Definition
The ability of objects to take on multiple forms. Methods with the same name behave differently based on the object calling them or the parameters passed.

### Implementation in Project

#### Method Overriding
```java
// Base Repository interface
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {
}

// Specific repository implementations (via Spring Data)
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Inherited common methods: save(), findById(), delete(), etc.
    
    // Product-specific methods
    Optional<Product> findBySku(String sku);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByStockQuantityLessThanEqual(Integer quantity);
}

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Inherited common methods work polymorphically
    // Category-specific methods
    Optional<Category> findByNameIgnoreCase(String name);
}
```

#### Polymorphic Service Methods
```java
public interface EntityService<T> {
    T create(T entity);
    T update(Long id, T entity);
    void delete(Long id);
    List<T> getAll();
}

@Service
public class ProductService implements EntityService<Product> {
    @Override
    @Transactional
    public Product create(Product product) {
        validateProduct(product);
        return productRepository.save(product);
    }
    
    @Override
    @Transactional
    public Product update(Long id, Product product) {
        Product existing = getProductById(id);
        updateFields(existing, product);
        return productRepository.save(existing);
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}

@Service
public class CategoryService implements EntityService<Category> {
    @Override
    @Transactional
    public Category create(Category category) {
        validateCategory(category);
        return categoryRepository.save(category);
    }
    
    // Polymorphic behavior - same interface, different implementation
}
```

#### Polymorphic HTTP Response Handling
```java
@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    // Generic response handling
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);  // 200 OK
    }
    
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product created = productService.createProduct(product);
        return new ResponseEntity<>(created, HttpStatus.CREATED);  // 201 CREATED
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();  // 204 NO CONTENT
    }
}
```

**Benefits**:
- Same interface for different types
- Flexible implementation
- Easy to add new entity types
- Loose coupling between components

## 4. Abstraction

### Definition
Hiding complex implementation details and exposing only necessary features to the user.

### Implementation in Project

#### Service Layer Abstraction
```java
// User doesn't need to know repository complexity
@Service
public class InventoryService {
    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final PurchaseRepository purchaseRepository;
    
    // Complex logic hidden behind simple interface
    @Transactional
    public void updateInventoryAfterSale(Long productId, Integer quantity) {
        // Internal: Complex transaction management, validation, logging
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        
        if (product.getStockQuantity() < quantity) {
            throw new InsufficientStockException("Insufficient stock available");
        }
        
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);
    }
    
    // Controller calls simple method
    @GetMapping("/low-stock")
    public ResponseEntity<List<LowStockProductDto>> getLowStockProducts() {
        // Implementation details hidden
        return ResponseEntity.ok(inventoryService.getLowStockProducts());
    }
}
```

#### DTO (Data Transfer Object) Abstraction
```java
// Hides internal entity structure
@Data
@AllArgsConstructor
public class LowStockProductDto {
    private Long productId;
    private String sku;
    private String productName;
    private Integer currentStock;
    private Integer reorderLevel;
    private String categoryName;
}

// Controller abstracts entity details
@Service
public class ReportService {
    @Transactional(readOnly = true)
    public List<LowStockProductDto> getLowStockProducts() {
        return productRepository.findLowStockProducts()
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    private LowStockProductDto mapToDTO(Product product) {
        return new LowStockProductDto(
            product.getId(),
            product.getSku(),
            product.getName(),
            product.getStockQuantity(),
            product.getReorderLevel(),
            product.getCategory().getName()
        );
    }
}
```

#### Repository Abstraction
```java
// SQL queries hidden behind method names
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Query complexity hidden
    Optional<Product> findBySku(String sku);
    
    List<Product> findByNameContainingIgnoreCase(String name);
    
    List<Product> findByStockQuantityLessThanEqual(Integer quantity);
    
    // Complex custom query abstracted as simple method
    @Query("SELECT p FROM Product p WHERE p.isActive = true " +
           "AND p.stockQuantity <= p.reorderLevel " +
           "ORDER BY p.stockQuantity ASC")
    List<Product> findLowStockProducts();
}
```

**Benefits**:
- Complex logic simplified for end-users
- Easy to understand public API
- Internal changes don't affect external code
- Reduced cognitive load

## 5. Association

### Definition
Relationship between classes representing how objects interact.

#### One-to-Many Association
```java
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    // One Category has Many Products
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();
}

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Many Products belong to One Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
```

#### Many-to-Many Association
```java
@Entity
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // One Sale has Many SaleItems
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> items = new ArrayList<>();
}

@Entity
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Many SaleItems belong to One Sale
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    private Sale sale;
    
    // Many SaleItems reference One Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
```

**Benefits**:
- Maintains referential integrity
- Enables complex queries
- Supports cascading operations
- Database normalization

---

# SYSTEM ARCHITECTURE

## High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     PRESENTATION LAYER                      │
│        React Frontend (Web Browser Application)             │
│  • Dashboard • Products • Categories • Suppliers            │
│  • Inventory • Sales • Purchases • Reports                  │
└────────────────────┬────────────────────────────────────────┘
                     │ HTTP/HTTPS, JSON
                     │
        ┌────────────▼────────────┐
        │   API GATEWAY / CORS    │
        │  Authentication/Token   │
        └────────────┬────────────┘
                     │ RESTful API Requests
                     │
┌────────────────────▼────────────────────────────────────────┐
│                APPLICATION LAYER                            │
│                  (Spring Boot Backend)                      │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐  │
│  │         REST CONTROLLER LAYER                       │  │
│  │  • ProductController   • CategoryController         │  │
│  │  • SupplierController  • InventoryController        │  │
│  │  • SalesController     • PurchaseController         │  │
│  │  • ReportController    • DashboardController        │  │
│  │  • AuthController      • UserController             │  │
│  └──────────────────────┬──────────────────────────────┘  │
│                         │                                  │
│  ┌──────────────────────▼──────────────────────────────┐  │
│  │         SERVICE LAYER (Business Logic)             │  │
│  │  • ProductService      • InventoryService          │  │
│  │  • CategoryService     • SalesService              │  │
│  │  • SupplierService     • PurchaseService           │  │
│  │  • ReportService       • DashboardService          │  │
│  │  • AuthService         • UserService               │  │
│  └──────────────────────┬──────────────────────────────┘  │
│                         │                                  │
│  ┌──────────────────────▼──────────────────────────────┐  │
│  │    PERSISTENCE LAYER (Data Access Object)          │  │
│  │  • ProductRepository    • CategoryRepository        │  │
│  │  • SupplierRepository   • InventoryRepository       │  │
│  │  • SaleRepository       • PurchaseRepository        │  │
│  │  • UserRepository       • RoleRepository            │  │
│  └──────────────────────┬──────────────────────────────┘  │
│                         │                                  │
│  ┌──────────────────────▼──────────────────────────────┐  │
│  │    UTILITY & HELPER LAYER                          │  │
│  │  • Validators          • Converters                │  │
│  │  • Exceptions          • Constants                 │  │
│  │  • Utilities           • Configurations            │  │
│  └──────────────────────┬──────────────────────────────┘  │
│                         │                                  │
└─────────────────────────┼──────────────────────────────────┘
                          │
┌─────────────────────────▼──────────────────────────────────┐
│                    DATA LAYER                              │
│                                                            │
│  ┌──────────────────────────────────────────────────┐    │
│  │         MySQL Database (8.4)                     │    │
│  │  • products           • categories              │    │
│  │  • suppliers          • inventory               │    │
│  │  • sales              • sale_items              │    │
│  │  • purchases          • purchase_items          │    │
│  │  • users              • roles                   │    │
│  │  • transaction_logs   • audit_trails            │    │
│  └──────────────────────────────────────────────────┘    │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

## Layered Architecture Details

### 1. Presentation Layer (Frontend)
- **Technology**: React 18.3.1, Vite 5.3.1
- **Responsibility**: User interface and user interactions
- **Components**:
  - Pages: Dashboard, Products, Categories, Suppliers, Inventory, Sales, Purchases, Reports
  - Components: Navbar, Sidebar, Table, Modal, Charts
  - Hooks: Custom React hooks for state management
  - Context: Authentication context for state management
  - Services: Axios HTTP client for API communication

### 2. Application Layer (Backend - Spring Boot)

#### 2.1 Controller Layer
- **Responsibility**: Handle HTTP requests and responses
- **Annotations**: @RestController, @RequestMapping, @GetMapping, @PostMapping, @PutMapping, @DeleteMapping
- **Features**: 
  - Request validation using @Valid
  - Exception handling
  - API documentation with Swagger

#### 2.2 Service Layer
- **Responsibility**: Implement business logic
- **Annotations**: @Service, @Transactional
- **Features**:
  - CRUD operations
  - Business logic implementation
  - Transaction management
  - Exception handling

#### 2.3 Repository Layer
- **Responsibility**: Data access and persistence
- **Technology**: Spring Data JPA
- **Features**:
  - Custom query methods
  - Automatic CRUD operations
  - Database interaction abstraction

### 3. Data Layer
- **Technology**: MySQL 8.4
- **Features**:
  - Relational schema
  - Indexes for performance
  - Constraints for data integrity
  - Stored procedures (optional)

## Request-Response Flow

```
User Action in React Frontend
         │
         ▼
HTTP Request (Axios)
    GET /api/products
    Authorization: Bearer <JWT_TOKEN>
         │
         ▼
REST Controller
    ProductController.getAllProducts()
         │
         ▼
Service Layer
    ProductService.getAllProducts()
         │
         ▼
Repository Layer
    ProductRepository.findAll()
         │
         ▼
Database Query
    SELECT * FROM products
         │
         ▼
Database Returns Results
    List<Product> productList
         │
         ▼
Service Processes Data
    Apply business logic, transform if needed
         │
         ▼
Controller Prepares Response
    ResponseEntity<List<Product>> with 200 OK
         │
         ▼
JSON Serialization
    Convert Java objects to JSON
         │
         ▼
HTTP Response
    [{"id": 1, "name": "Laptop", ...}, ...]
    Content-Type: application/json
         │
         ▼
React Frontend
    Update component state with data
    Re-render component
    Display products in table/list
```

## Module Dependencies

```
Authentication Module
    └─ JWT Token Generation and Validation
    └─ User Service

Product Module
    └─ Product Repository
    └─ Product Service
    └─ Product Controller
    └─ Category Module (dependency)
    └─ Supplier Module (dependency)

Inventory Module
    └─ Product Module
    └─ Inventory Service
    └─ Stock Level Management

Sales Module
    └─ Product Module
    └─ Sale Service
    └─ Inventory Module (to update stock)

Purchase Module
    └─ Product Module
    └─ Supplier Module
    └─ Purchase Service
    └─ Inventory Module

Report Module
    └─ Sales Module
    └─ Purchase Module
    └─ Product Module
    └─ Report Service

Dashboard Module
    └─ Report Module
    └─ Inventory Module
    └─ Dashboard Service
```

---

