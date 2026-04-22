# 🌿 Verdana — Eco Sustainability Website

> **Made by Anurag Kumar**  
> A premium full-stack environmental website combining a stunning animated frontend with a robust Spring Boot backend.

---

## 🗂 Project Structure

```
verdana/
├── index.html                   ← Frontend (single HTML file, zero dependencies)
└── verdana-backend/             ← Spring Boot backend
    ├── pom.xml
    └── src/main/
        ├── java/com/verdana/contact/
        │   ├── VerdanaApplication.java          ← Entry point
        │   ├── controller/ContactController.java ← REST API
        │   ├── service/
        │   │   ├── ContactService.java           ← Business logic
        │   │   └── EmailService.java             ← HTML email dispatch
        │   ├── model/ContactMessage.java         ← JPA entity
        │   ├── repository/ContactRepository.java ← Spring Data JPA
        │   ├── dto/
        │   │   ├── ContactRequest.java           ← Validated input DTO
        │   │   └── ApiResponse.java              ← Standardised response
        │   ├── config/WebConfig.java             ← CORS + Async
        │   └── exception/GlobalExceptionHandler.java ← Clean error handling
        └── resources/
            ├── application.properties           ← Config (DB, mail, etc.)
            └── schema.sql                       ← MySQL DDL
```

---

## ✨ Features

### Frontend
- 🌿 **Nature-inspired loading animation** — growing plant stem with leaves
- 🖱️ **Custom cursor** — dot + ring following mouse
- 🍃 **Floating animated leaves** — scattered across the entire page
- 🎞️ **Scroll-reveal animations** — fade-in/up, slide-left/right
- 🌲 **SVG forest illustration** in hero section
- 📊 **Animated counters** — numbers count up on scroll
- 🎨 **Deep green palette** — forest, rich, fresh, light green + earthy creams
- 📱 **Fully responsive** — mobile-first with hamburger nav
- ♿ **SEO optimised** — meta tags, semantic HTML
- 📬 **Contact form** — validates and submits to backend API

### Backend (Spring Boot 3)
- ✅ `POST /api/contact` — Accepts, validates, stores, and emails on form submit
- ✅ `GET  /api/admin/contact` — Lists all messages newest-first
- ✅ `GET  /api/admin/contact/{id}` — Single message retrieval
- ✅ `PUT  /api/admin/contact/{id}/read` — Mark as read
- ✅ `GET  /api/admin/contact/stats` — Counts (total, unread, read)
- ✅ `GET  /api/health` — Health check
- 📧 **Owner notification email** — Rich HTML email sent to `luckyanurag9045@gmail.com`
- 📧 **Auto-reply to submitter** — Professional confirmation email
- 🗄️ **MySQL persistence** — `contact_messages` table with proper indexes
- 🛡️ **Bean Validation** — `@Valid` on DTO, clean `400` error responses
- 🌐 **CORS configured** — allows dev + prod origins

---

## 🚀 Quick Start

### 1 — Database Setup
```bash
# Login as MySQL root
mysql -u root -p

# Run the schema script
source verdana-backend/src/main/resources/schema.sql
```

### 2 — Configure Email (Gmail App Password)
1. Enable 2-Factor Authentication on your Google account
2. Go to [myaccount.google.com/apppasswords](https://myaccount.google.com/apppasswords)
3. Create a new app password for "Mail"
4. Update `application.properties`:
```properties
spring.mail.username=luckyanurag9045@gmail.com
spring.mail.password=YOUR_16_CHAR_APP_PASSWORD
```

### 3 — Run Backend
```bash
cd verdana-backend
./mvnw spring-boot:run
# Server starts on http://localhost:8080
```

### 4 — Open Frontend
Simply open `index.html` in a browser (via VS Code Live Server or file://).

The contact form auto-connects to `http://localhost:8080/api/contact`.

---

## 🔌 API Reference

### POST /api/contact
```json
// Request body
{
  "name":    "Anurag Kumar",
  "email":   "user@example.com",
  "subject": "Partnership inquiry",
  "message": "Hello, I'd love to collaborate..."
}

// Success response (201)
{
  "success": true,
  "message": "Thank you Anurag Kumar! Your message has been received...",
  "data": {
    "id": 1,
    "submittedAt": "2025-04-22T10:30:00"
  },
  "timestamp": "2025-04-22T10:30:00"
}

// Validation error response (400)
{
  "success": false,
  "message": "Validation failed — please check your input",
  "data": {
    "email": "Please enter a valid email address",
    "message": "Message must be 10–3000 characters"
  }
}
```

---

## 🛠 Tech Stack

| Layer      | Technology                              |
|------------|-----------------------------------------|
| Frontend   | HTML5, CSS3 (animations), Vanilla JS    |
| Backend    | Java 17, Spring Boot 3.2, Spring Data   |
| Database   | MySQL 8.x                               |
| Email      | Spring Mail + Gmail SMTP                |
| Build      | Maven                                   |
| ORM        | Hibernate / JPA                         |

---

## 📄 Colour Palette

| Name     | Hex       |
|----------|-----------|
| Forest   | `#0d2818` |
| Deep     | `#1a4a2e` |
| Rich     | `#2d6a4f` |
| Mid      | `#40916c` |
| Fresh    | `#52b788` |
| Light    | `#95d5b2` |
| Pale     | `#d8f3dc` |
| Cream    | `#f4ede0` |

---

## 👤 Author

**Anurag Kumar**  
📧 luckyanurag9045@gmail.com  
💼 [LinkedIn](https://www.linkedin.com/in/anurag-kumar-1b967729b/)  
🐙 [GitHub](https://github.com/anuragkumar103)

---

*Made with 💚 by Anurag Kumar · Verdana Eco Initiative*
