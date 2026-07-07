# Smart Inventory Management System Database Design

## ER Diagram

```text
roles 1---* users
categories 1---* products
suppliers 1---* products
products 1---* inventory
products 1---* sale_items
sales 1---* sale_items
products 1---* purchase_items
purchases 1---* purchase_items
users 1---* sales
users 1---* purchases
suppliers 1---* purchases
```

## Tables

- users
- roles
- products
- categories
- suppliers
- sales
- sale_items
- purchases
- purchase_items
- inventory

## Primary Keys

- roles.id
- users.id
- categories.id
- suppliers.id
- products.id
- inventory.id
- sales.id
- sale_items.id
- purchases.id
- purchase_items.id

## Foreign Keys

- users.role_id -> roles.id
- products.category_id -> categories.id
- products.supplier_id -> suppliers.id
- inventory.product_id -> products.id
- sale_items.sale_id -> sales.id
- sale_items.product_id -> products.id
- sales.created_by -> users.id
- purchases.supplier_id -> suppliers.id
- purchases.created_by -> users.id
- purchase_items.purchase_id -> purchases.id
- purchase_items.product_id -> products.id

## Relationships

- One role has many users.
- One category has many products.
- One supplier can supply many products.
- One product has one inventory record.
- One sale has many sale items.
- One product can appear in many sale items.
- One purchase has many purchase items.
- One product can appear in many purchase items.
- One user can create many sales and purchases.

## Notes

- The schema is designed for a typical inventory and sales workflow.
- You can import it into MySQL with:

```bash
mysql -u your_user -p < backend/src/main/resources/db/smart_inventory_schema.sql
```
