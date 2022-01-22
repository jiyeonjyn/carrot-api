# springboot-api
 
| 프론트 | 방향 | 서버 |
| --- | --- | --- |
| User |  |  |
| POST /auth
requestbody에 phone | → | SMS 인증 요청 |
| POST /auth/verify
requestbody에 phone, verifyNum | → | 결과 맞는지 확인 |
| 오류코드 3014면 추가 정보 입력 페이지로
jwt 전달 받으면 GET /products로 | ← | 기존 유저 있으면 로그인 진행
(jwt 생성하여 전달),
기존 유저 없으면 3014 오류 |
| POST /users
requestbody에 user 정보 | → | addUser 후 로그인 진행
(jwt 생성하여 전달) |
| GET /users | → | 관리자일 때만 페이지 보여줌
관리자가 아닌 경우 2003 오류 |
| GET /users/{userIdx} | → | 해당 유저 상세페이지 |
| Product |  |  |
| GET /products | → | jwt에서 userIdx 얻어
해당 지역의 상품 목록 조회 |
| GET /products?writer={userIdx}&status={status} | → | 특정 유저의 판매 상품 목록 조회 |
| GET /products/{productIdx} | → | 유저와 게시글 작성자의 userIdx가 다르면 ViewCount 테이블에 기록 |
| POST /products
requestbody에 게시글 내용 | → | jwt에서 userIdx 얻어
requestbody에 set |
| PUT /products
requestbody에 게시글 내용 | → | 컨트롤러에서 유저와 게시글 작성자의 userIdx가 같은지 체크 |
| DELECT /products/{productIdx} | → | 서비스에서 유저와 게시글 작성자의 userIdx가 같은지 체크 |
| GET /categories/{categoryIdx}/products | → | jwt에서 userIdx 얻어 해당 카테고리,
해당 지역의 상품 목록 조회 |
