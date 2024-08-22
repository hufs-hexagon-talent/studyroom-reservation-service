# Study Room Reservation Service API
이 프로젝트는 한국외국어대학교 공과대학 스터디룸 서비스를 위해 구축된 스터디룸 예약 서비스 API입니다.

## Table of Contents
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)
- ~~[License](#license)~~

## Features
- **Room Reservation**: 사용자가 정의된 정책으로 스터디룸을 예약할 수 있습니다.
- **Reservation CheckIn** : 사용자가 예약한 스터디룸에 체크인할 수 있습니다.
- **User Management**: 사용자 인증 및 권한 부여를 처리합니다.
- **Room Operation Policies & Schedules**: 세미나실 사용에 대한 구체적인 규칙을 정의하고, 세미나실 이용 가능 여부와 일정을 관리합니다.
- **JWT Authentication**: JWT 토큰을 사용하여 API 엔드포인트를 보호합니다.
- **API Documentation**: Swagger UI를 통해 API를 종합적으로 문서화합니다.
- **Continuous Integration/Deployment**: GitHub 액션을 사용하여 통합 CI/CD 파이프라인을 사용합니다.

## Technology Stack
<code><img title="Mysql" alt="mysql" width="40px"
src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original-wordmark.svg" /></code>
<code><img title="Redis" alt="redis" width="40px"
src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/redis/redis-original-wordmark.svg" /></code>
<code><img title="Docker" alt="docker" width="40px"
src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/docker/docker-original.svg"/></code>
<code><img title="GithubActions" alt="githubactions" width="40px"
src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/githubactions/githubactions-original.svg" /></code>
<code><img title="Spring" alt="spring" width="40px"
src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" /></code>
<code><img title="Swagger" alt="swagger" width="40px"
src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/swagger/swagger-original.svg" /></code>
<code><img title="Postman" alt="postman" width="40px"
src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/postman/postman-original.svg" /></code>
### Framework
- **Spring Boot 3.2.2**

### Language
- **Java 17**

### ORM
- **Spring Data JPA**

### Database
- **MySQL 8.0**
- **Redis 7.2**  

### Security
- **Spring Security 6.2.2**
- **JWT (jjwt-api:0.12.3)**

### API Client
- **React**: Frontend client.

### API Documentation
- **Swagger UI**
- **Postman**

### Containerization
- **Docker**
- **Docker Compose**

### CI/CD
- **GitHub Actions**  
  

## Architecture
![세미나실 drawio](https://github.com/user-attachments/assets/b199cfad-2a2e-42fa-bec0-151d8c2ed8f1)


## Contributing
개선 사항이 있으면 이슈를 열거나 풀 리퀘스트를 제출해 주세요.
