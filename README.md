# 실전! 스프링 부트와 JPA 활용

## 📚 강의 정보
- **강의명**: 실전! 스프링 부트와 JPA 활용1 - 웹 애플리케이션 개발
- **강사**: 김영한
- **플랫폼**: 인프런

## 🎯 학습 목표
- 스프링 부트와 JPA를 활용한 웹 애플리케이션 개발
- 실무에서 사용하는 JPA 활용법 학습
- 도메인 주도 설계(DDD) 적용
- 테스트 주도 개발(TDD) 실습

## 📝 강의 내용

### 1. 프로젝트 환경설정
- [ ] 프로젝트 생성
- [ ] 라이브러리 살펴보기
- [ ] View 환경 설정
- [ ] H2 데이터베이스 설치
- [ ] JPA와 DB 설정, 동작확인

### 2. 도메인 분석 설계
- [ ] 요구사항 분석
- [ ] 도메인 모델과 테이블 설계
- [ ] 엔티티 클래스 개발1
- [ ] 엔티티 클래스 개발2
- [ ] 엔티티 설계시 주의점

### 3. 애플리케이션 구현 준비
- [ ] 구현 요구사항
- [ ] 애플리케이션 아키텍처
- [ ] 회원 도메인 개발
- [ ] 회원 리포지토리 개발
- [ ] 회원 서비스 개발
- [ ] 회원 기능 테스트

### 4. 상품 도메인 개발
- [ ] 상품 엔티티 개발(비즈니스 로직 추가)
- [ ] 상품 리포지토리 개발
- [ ] 상품 서비스 개발

### 5. 주문 도메인 개발
- [ ] 주문, 주문상품 엔티티 개발
- [ ] 주문 리포지토리 개발
- [ ] 주문 서비스 개발
- [ ] 주문 기능 테스트
- [ ] 주문 검색 기능 개발

### 6. 웹 계층 개발
- [ ] 홈 화면과 레이아웃
- [ ] 회원 등록
- [ ] 회원 목록 조회
- [ ] 상품 등록
- [ ] 상품 목록
- [ ] 상품 수정
- [ ] 변경 감지와 병합(merge)
- [ ] 상품 주문
- [ ] 주문 목록 검색, 취소

### 7. 웹 계층 개발 (API)
- [ ] 회원 등록 API
- [ ] 회원 수정 API
- [ ] 회원 조회 API
- [ ] 주문 조회 API

## 🔧 기술 스택
- **Framework**: Spring Boot 3.x
- **ORM**: JPA (Hibernate)
- **Database**: H2 Database (개발), MySQL/PostgreSQL (운영)
- **Template Engine**: Thymeleaf
- **Build Tool**: Gradle
- **Test**: JUnit 5

## 📂 프로젝트 구조
```
src/
├── main/
│   ├── java/
│   │   └── jpabook/
│   │       └── jpashop/
│   │           ├── JpashopApplication.java
│   │           ├── controller/
│   │           ├── domain/
│   │           ├── repository/
│   │           └── service/
│   └── resources/
│       ├── application.yml
│       ├── static/
│       └── templates/
└── test/
    └── java/
        └── jpabook/
            └── jpashop/
```

## 📖 학습 노트

### 주요 개념
- **엔티티 매핑**: `@Entity`, `@Table`, `@Column`
- **연관관계 매핑**: `@OneToMany`, `@ManyToOne`, `@ManyToMany`
- **영속성 컨텍스트**: 1차 캐시, 변경 감지, 지연 로딩
- **JPQL**: 객체지향 쿼리 언어

### 실습 코드
```java
// 주요 코드 스니펫들을 여기에 추가
```

### 트러블슈팅
- 문제 상황과 해결 방법들을 기록

## 🎓 학습 진도
- **시작일**: YYYY-MM-DD
- **목표 완료일**: YYYY-MM-DD
- **현재 진도**: 섹션 X 완료

## 📚 참고 자료
- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [JPA 공식 문서](https://jakarta.ee/specifications/persistence/)
- [Hibernate 공식 문서](https://hibernate.org/orm/documentation/)

## 🤝 기여하기
이 저장소는 개인 학습 목적으로 만들어졌습니다. 궁금한 점이나 개선사항이 있으면 이슈를 등록해주세요.

## 📄 라이선스
이 프로젝트는 학습 목적으로만 사용됩니다.
