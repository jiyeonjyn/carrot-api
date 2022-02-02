# 당근마켓 클론 API

- Base URL : ~~https://www.jiyeon.site/ (Expired)~~  https://3.37.202.234 (You can test it on Postman.)
- Server : AWS EC2
- Server Program : Nginx
- Database : AWS RDS
- RDBMS : MySQL
- Build with Gradle
<br>

**User**

| API                  | 프론트                                       | 방향 | 서버                                                                                 |
| -------------------- | -------------------------------------------- | ---- | ------------------------------------------------------------------------------------ |
| POST /auth           | (requestbody) phone                          | →    | SMS 인증 요청                                                                        |
| POST /auth/verify    | (requestbody) phone, verifyNum               | →    | 인증 결과 확인                                                                       |
|                      | jwt 전달 받으면 GET /products로 이동         | ←    | 기존 유저 있으면 로그인 진행<br>(jwt 생성하여 전달)                                   |
|                      | 오류코드 3014 : 가입 정보 입력 페이지로 이동 | ←    | 기존 유저 없으면 3014 오류 전달                                                       |
| POST /login          | (requestbody) user의 휴대폰 번호             | →    | 기존 유저 있으면 로그인 진행<br>(jwt 생성하여 전달)<br> 기존 유저 없으면 3014 오류 전달|
| POST /users          | (requestbody) user 정보                      | →    | addUser 후 로그인 진행<br>(jwt 생성하여 전달)                                         |
| GET /users           | (header) jwt                                 | →    | 관리자면 전체 user 정보 조회<br>관리자가 아니면 2003 오류 전달                        |
| GET /users/{userIdx} | (header) jwt                                 | →    | 해당 유저 상세페이지 조회                                                             |

<br>

**Product**

| API                                            | 프론트                                   | 방향 | 서버                                                                                         |
| ---------------------------------------------- | ---------------------------------------- | ---- | -------------------------------------------------------------------------------------------- |
| GET /products                                  | (header) jwt                             | →    | jwt에서 userIdx 얻어 유저의 address에 해당하는 지역의 상품 목록 조회                         |
| GET /products?writer={userIdx}&status={status} | (header) jwt                             | →    | 특정 유저의 판매 상품 목록 조회                                                              |
| GET /products/{productIdx}                     | (header) jwt                             | →    | 유저와 상품 판매자의 userIdx가 다르면 ViewCount 테이블에 기록                                |
| POST /products                                 | (header) jwt <br>(requestbody) 상품 내용 | →    | jwt에서 userIdx 얻어 상품 등록                                                               |
| PUT /products                                  | (header) jwt <br>(requestbody) 상품 내용 | →    | 유저와 상품 판매자의 userIdx가 같은지 체크한 후 같으면 상품 수정                             |
| DELETE /products/{productIdx}                  | (header) jwt                             | →    | 유저와 상품 판매자의 userIdx가 같은지 체크한 후 같으면 상품 삭제                             |
| GET /categories/{categoryIdx}/products         | (header) jwt                             | →    | jwt에서 userIdx 얻어 해당 카테고리의 상품 중 유저의 address에 해당하는 지역의 상품 목록 조회 |
