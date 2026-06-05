# Mini Order E2E

Spring Boot 기반 미니 주문 API를 구현하고 Vue 화면과 연동했습니다.  
상품 조회, 장바구니 담기(중복 상품 수량 합산 처리), 주문 생성(재고 차감), 주문 내역 확인 흐름을 Playwright E2E 테스트로 자동 검증했습니다.

---

## 기술 스택

| 영역 | 기술 |
|------|------|
| Backend | Java 17, Spring Boot, Spring Data JPA, H2, Validation, Lombok, Gradle |
| Frontend | Vue 3, Vite, Vue Router, Axios |
| E2E Test | Playwright, TypeScript |
| Tool | VS Code, Git |

---

## 프로젝트 구조

```
mini-order-e2e/
├── backend/               # Spring Boot API 서버
│   └── src/main/java/com/example/miniorder/
│       ├── domain/
│       │   ├── product/   # 상품 Entity, API
│       │   ├── cart/      # 장바구니 Entity, API
│       │   └── order/     # 주문 Entity, API
│       └── global/
│           ├── config/    # CORS 설정
│           └── exception/ # 전역 예외 처리
├── frontend/              # Vue 3 화면
│   └── src/
│       ├── api/           # Axios API 호출 함수
│       └── components/    # 페이지 컴포넌트
├── e2e/                   # Playwright E2E 테스트
│   └── tests/
│       └── order-flow.spec.ts
└── requests/              # HTTP 요청 테스트 파일
```

---

## 구현 기능

**Backend**
- 상품 목록 조회 (`GET /api/products`)
- 장바구니 상품 추가 — 동일 상품 재담기 시 수량 합산 처리 (`POST /api/cart/items`)
- 장바구니 조회 (`GET /api/cart/items`)
- 장바구니 개별 삭제 (`DELETE /api/cart/items/{id}`)
- 장바구니 전체 비우기 (`DELETE /api/cart/items`)
- 주문 생성 — 재고 차감, 장바구니 자동 비우기 (`POST /api/orders`)
- 주문 내역 조회 (`GET /api/orders`)
- 전역 예외 처리 — `IllegalArgumentException`, `MethodArgumentNotValidException` JSON 응답

**Frontend**
- 상품 목록 화면 — 수량 선택(+/-), 장바구니 담기, 토스트 메시지
- 장바구니 화면 — 상품 목록, 총 금액, 개별 삭제, 전체 비우기, 주문하기
- 주문 완료 화면
- 주문 내역 화면

**E2E Test**
- 상품 목록 표시 확인
- 장바구니 담기 → 주문 완료 흐름 확인
- 주문 내역 확인

---

## API 명세

| Method | URL | 설명 |
|--------|-----|------|
| GET | /api/health | 서버 상태 확인 |
| GET | /api/products | 상품 목록 조회 |
| POST | /api/cart/items | 장바구니 상품 추가 |
| GET | /api/cart/items | 장바구니 조회 |
| DELETE | /api/cart/items/{id} | 장바구니 개별 삭제 |
| DELETE | /api/cart/items | 장바구니 전체 비우기 |
| POST | /api/orders | 주문 생성 |
| GET | /api/orders | 주문 내역 조회 |

---

## 로컬 실행 방법

### 1. 백엔드 실행

```bash
cd backend
./gradlew bootRun   # Mac / Linux
.\gradlew bootRun   # Windows
```

- 서버 주소: `http://localhost:8080`
- H2 Console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:miniorder`
  - User Name: `sa` / Password: 없음

### 2. 프론트엔드 실행

```bash
cd frontend
npm install
npm run dev
```

- 화면 주소: `http://localhost:5173`

### 3. E2E 테스트 실행

백엔드와 프론트엔드가 모두 실행 중인 상태에서 실행하세요.

```bash
cd e2e
npx playwright test           # headless 모드
npx playwright test --headed  # 브라우저 직접 확인
npx playwright show-report    # 테스트 리포트 확인
```

---

## 트러블슈팅

### 1. 장바구니 중복 상품 추가 시 신규 생성 문제
**현상** — 같은 상품을 여러 번 담으면 CartItem이 중복 생성되어 주문 시 재고가 의도보다 많이 차감됨  
**해결** — `CartItemRepository`에 `findByProductId()` 추가, 동일 상품이 이미 있으면 수량을 합산하도록 `CartItem.addQuantity()` 구현

### 2. GlobalExceptionHandler 미적용으로 에러 응답 파악 어려움
**현상** — `MethodArgumentNotValidException` 발생 시 Spring 기본 HTML 에러 페이지 반환  
**해결** — `@RestControllerAdvice`로 `GlobalExceptionHandler` 구현, `{ "message": "..." }` JSON 형태로 통일

### 3. Playwright 테스트에서 API 응답 타이밍 문제
**현상** — 페이지 이동 후 API 응답 전에 요소를 검증해서 `element(s) not found` 오류 발생  
**해결** — `page.waitForResponse()`를 클릭 이벤트보다 먼저 선언해두고, 응답을 기다린 후 검증하도록 순서 변경

### 4. Playwright 반복 실행 시 재고 부족으로 테스트 실패
**현상** — H2 인메모리 DB 특성상 테스트를 반복 실행하면 재고가 계속 차감되어 주문 실패  
**해결** — 테스트 재실행 전 백엔드 서버를 재시작해 DB를 초기화

### 5. Playwright strict mode violation
**현상** — 주문 내역 페이지에서 `getByText("노트북")`이 여러 주문에 걸쳐 중복 요소를 찾아 오류 발생  
**해결** — `.first()`를 추가해 첫 번째 요소만 검증하도록 수정

---

## 추후 개발 계획

### 기능 개선
- **동시성 처리** — 현재 재고 검증은 백엔드 단에서만 처리하고 있어 동시 요청 시 재고 불일치가 발생할 수 있음. 낙관적 락(`@Version`) 적용 고려
- **주문 취소** — `OrderStatus.CANCELED`는 선언만 된 상태. 취소 API(`PATCH /api/orders/{id}/cancel`) 구현
- **장바구니 수량 변경** — 현재 삭제 후 재담기로 대체 중. `PATCH /api/cart/items/{id}` API 추가

### 테스트 개선
- **테스트 격리** — 현재 테스트가 실제 DB를 공유해서 반복 실행 시 데이터가 누적됨. `@Transactional` 롤백 또는 테스트 전용 데이터 초기화 API 도입 고려
- **테스트 케이스 추가** — 재고 부족 주문 시 에러 메시지 표시 확인, 빈 장바구니 주문 시도 확인 등 예외 흐름 테스트 추가

### 인프라
- **DB 교체** — H2 인메모리 DB에서 MySQL 또는 PostgreSQL로 전환
- **배포** — Docker 컨테이너화 및 클라우드 배포 (AWS EC2 또는 Railway)
