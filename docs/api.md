# Book Marketplace API Documentation

## Authentication

All API endpoints except `/api/auth/**` require JWT authentication. Include the JWT token in the Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

## API Endpoints

### Authentication

#### Register Customer
```http
POST /api/auth/register
Content-Type: application/json

{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "securepassword"
}
```

Response:
```json
{
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "token": "jwt_token_here"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
    "email": "john@example.com",
    "password": "securepassword"
}
```

Response:
```json
{
    "token": "jwt_token_here"
}
```

### Books

#### List Books
```http
GET /api/books
```

Response:
```json
{
    "content": [
        {
            "id": 1,
            "name": "The Great Gatsby",
            "price": 19.99,
            "status": "AVAILABLE",
            "customer": null
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10
    },
    "totalElements": 1
}
```

#### Get Book
```http
GET /api/books/{id}
```

Response:
```json
{
    "id": 1,
    "name": "The Great Gatsby",
    "price": 19.99,
    "status": "AVAILABLE",
    "customer": null
}
```

#### Create Book
```http
POST /api/books
Content-Type: application/json

{
    "name": "The Great Gatsby",
    "price": 19.99
}
```

Response:
```json
{
    "id": 1,
    "name": "The Great Gatsby",
    "price": 19.99,
    "status": "AVAILABLE",
    "customer": null
}
```

#### Update Book
```http
PUT /api/books/{id}
Content-Type: application/json

{
    "name": "The Great Gatsby (Updated)",
    "price": 24.99
}
```

Response:
```json
{
    "id": 1,
    "name": "The Great Gatsby (Updated)",
    "price": 24.99,
    "status": "AVAILABLE",
    "customer": null
}
```

#### Delete Book
```http
DELETE /api/books/{id}
```

Response: 204 No Content

### Customers

#### Get Customer Profile
```http
GET /api/customers/{id}
```

Response:
```json
{
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com"
}
```

#### Update Customer Profile
```http
PUT /api/customers/{id}
Content-Type: application/json

{
    "name": "John Doe (Updated)",
    "email": "john.updated@example.com"
}
```

Response:
```json
{
    "id": 1,
    "name": "John Doe (Updated)",
    "email": "john.updated@example.com"
}
```

### Purchases

#### Create Purchase
```http
POST /api/purchases
Content-Type: application/json

{
    "bookId": 1,
    "customerId": 1
}
```

Response:
```json
{
    "id": 1,
    "book": {
        "id": 1,
        "name": "The Great Gatsby",
        "price": 19.99
    },
    "customer": {
        "id": 1,
        "name": "John Doe"
    },
    "amount": 19.99,
    "status": "COMPLETED"
}
```

#### Get Purchase
```http
GET /api/purchases/{id}
```

Response:
```json
{
    "id": 1,
    "book": {
        "id": 1,
        "name": "The Great Gatsby",
        "price": 19.99
    },
    "customer": {
        "id": 1,
        "name": "John Doe"
    },
    "amount": 19.99,
    "status": "COMPLETED"
}
```

## Error Responses

All error responses follow this format:

```json
{
    "code": "ERROR_CODE",
    "message": "Human readable error message",
    "timestamp": "2024-03-20T10:00:00Z"
}
```

Common error codes:
- `BM10001`: Book status update error
- `BM10002`: Book not found
- `BM10003`: Customer not found
- `BM10004`: Purchase failed
- `BM10005`: Authentication failed
- `BM10006`: Validation error

## Rate Limiting

API requests are limited to:
- 100 requests per minute for authenticated users
- 20 requests per minute for unauthenticated users

## Versioning

The current API version is v1. The version is included in the URL path: `/api/v1/...` 