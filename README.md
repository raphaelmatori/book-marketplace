# Book Marketplace

A Spring Boot-based marketplace application for buying and selling books, built with Kotlin.

## 🚀 Quick Start

### Prerequisites
- JDK 17 or higher
- Gradle 8.x
- MySQL 8.x

### Running the Application
```bash
# Clone the repository
git clone <repository-url>

# Navigate to project directory
cd book-marketplace

# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

## 📚 Documentation

- [Architecture Overview](docs/architecture.puml)
- [API Documentation](docs/api.md)
- [Database Schema](docs/database.puml)
- [Security Documentation](docs/security.md)
- [Development Guide](docs/development.md)

### Sequence Diagrams (Use Cases)
- [User Registration](docs/user_registration_sequence.puml)
- [User Login](docs/user_login_sequence.puml)
- [Book Purchase](docs/book_purchase_sequence.puml)
- [List Books](docs/list_books_sequence.puml)
- [Update Book](docs/update_book_sequence.puml)
- [Delete Book](docs/delete_book_sequence.puml)
- [Update Customer Profile](docs/update_customer_profile_sequence.puml)
- [Get Purchase Details](docs/get_purchase_details_sequence.puml)
- [List Purchases for Customer](docs/list_purchases_for_customer_sequence.puml)

## 🏗️ Architecture

The application follows a layered architecture pattern:

```
com.bookmarketplace/
├── config/         # Application configuration
├── controller/     # REST API endpoints
├── service/        # Business logic
├── repository/     # Data access
├── model/          # Domain entities
├── security/       # Authentication & authorization
├── validation/     # Input validation
├── events/         # Event handling
├── exception/      # Custom exceptions
├── enums/          # Enumerations
└── extension/      # Kotlin extensions
```

## 🔑 Key Features

- Book listing and management
- Customer registration and management
- Purchase processing
- JWT-based authentication
- Role-based access control
- Event-driven architecture (Spring @Async, not coroutines)
- Input validation
- Error handling

## 🛠️ Technology Stack

- Kotlin 1.9.x
- Spring Boot 3.x
- MySQL 8.x
- Gradle
- JWT for authentication
- JPA/Hibernate

## 🔒 Security

The application implements:
- JWT-based authentication
- Role-based access control
- Password encryption
- CORS configuration
- Input validation
- SQL injection prevention

## 🧪 Testing

```bash
# Run all tests
./gradlew test

# Run specific test category
./gradlew test --tests "com.bookmarketplace.*.BookServiceTest"
```

## 📦 Build & Deployment

### Building
```bash
./gradlew clean build
```

### Running with Docker
```bash
docker build -t book-marketplace .
docker run -p 8080:8080 book-marketplace
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- Your Name - Initial work

## 🙏 Acknowledgments

- Spring Boot team
- Kotlin team
- All contributors 