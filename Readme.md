# Diet Blog App

A simple web application for managing and tracking recipes with calorie information.

## Features
- View all recipes with calorie details
- Create and manage recipes
- User authentication (JWT or session cookies)
- Logs all user activities (including IP addresses)
- Dockerized deployment with PostgreSQL

## Tech Stack
- **Backend:** Java Servlets (4.0.4), JSP
- **Database:** PostgreSQL 14
- **Deployment:** Docker, Docker Compose

## Installation

### Prerequisites
- Docker & Docker Compose installed

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/diet-blog.git
   cd diet-blog
   ```
2. Start the application using Docker:
   ```bash
   docker-compose up --build
   ```
3. Access the application at:
   ```
   http://localhost:8080
   ```

## API Endpoints
| Method | Endpoint | Description |
|--------|---------|-------------|
| GET | /recipes | Get all recipes |
| POST | /recipes | Create a new recipe |
| POST | /auth/login | User login |
| POST | /auth/register | User registration |

## Logging
- All user actions, including IP addresses, are logged for security and analysis.

## Future Improvements
- Add nutrition analysis integration
- Improve UI with frontend framework

## Contributing
Feel free to fork this repository and submit a pull request.

##
Login:
Admin: email: john.doe@mail.com
       password: password1
User:  email: alice.jones@mail.com
       password: password2
       
## License
This project is licensed under the MIT License.

---
