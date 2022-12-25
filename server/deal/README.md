# 공구마켓 Deal 서버 API
---
## Table of Contents  
|게시글 관련|구매 관련|
|------|---|
|[게시글 조회](#게시글-조회)   |[구매 참여](#구매-참여)     |
|[게시글 상세 보기](#게시글-상세-보기)   |[구매 수정](#구매-수정)     |
|[게시글 작성](#게시글-작성) |[구매 취소](#구매-취소) |
|[게시글 수정](#게시글-수정)  |[참여 내역 조회](#참여-내역-조회)    |
|[게시글 삭제](#게시글-삭제) |[판매 내역 조회](#판매-내역-조회)   |

---

## 게시글 조회
### 요청
GET /deal?title=제목&category=카테고리&minPrice=100&maxPrice=1000&order=1
|**Parameter**|**Description**|**Optional**|**Constraint**|
| :- | :- | :- | :- |
|title|게시글 제목|Y|입력된 단어가 포함된 모든 게시글. '제목’입력했습니다.|
|category|게시글의 카테고리|Y|'카테고리’입력했습니다.|
|minPrice|최소 가격|Y||
|maxPrice|최대 가격|Y||
|order|게시글 정렬|Y|미입력시 최신순, 1 = 인기순, 2 = 적게 남은 수량 순|
### 응답
HTTP/1.1 200 OK
  
|**Path**|**Type**|**Description**|
| :- | :- | :- |
|[].id|Number|게시글 ID|
|[].category|String|제품 카테고리|
|[].title|String|게시글 제목|
|[].remainDate|Number|남은 날짜|
|[].unitPrice|Number|단위 가격|
|[].quantity|Number|제품 수량|
|[].nowCount|Number|현재 모집 수량|
|[].totalCount|Number|총 모집 수량|
|[].image.fileName|Null|이미지 이름|
|[].image.thumbnail|Boolean|썸네일 여부|
|[].deleted|Boolean|삭제여부|
|[].expired|Boolean|만료여부|
---
## 게시글 상세 보기
### 요청
GET /deal/{dealId} 

|**Parameter**|**Description**|**Optional**|**Constraint**|
| :- | :- | :- | :- |
|dealId|게시글 ID|||
### 응답
HTTP/1.1 200 OK
|**Path**|**Type**|**Description**|
| :- | :- | :- |
|id|Number|게시글 ID|
|title|String|게시글 제목|
|content|String|게시글 내용|
|remainDate|Number|남은 날짜|
|price|Number|가격|
|unitPrice|Number|단위 가격|
|quantity|Number|제품 수량|
|unitQuantity|Number|단위 수량|
|unit|String|단위|
|nowCount|Number|현재 모집 수량|
|totalCount|Number|총 모집 수량|
|url|String|상품 URL|
|view|Number|조회수|
|images[].fileName|String|이미지 이름|
|images[].thumbnail|Boolean|썸네일 여부|
|deleted|Boolean|삭제 여부|
|expired|Boolean|만료 여부|
|user|String|게시글 작성 유저 닉네임|
|category.id|Number|게시글 카테고리 ID|
|category.name|String|게시글 카테고리 이름|
|keywords|Array|게시글 키워드|
|expiredDate|String|게시글 만료일|
---
## 게시글 작성
### 요청
POST /deal 

|**Path**|**Type**|**Description**|**Optional**|**Constraint**|
| :- | :- | :- | :- | :- |
|title|String|게시글 제목|||
|content|String|센터 주소|||
|price|Number|제품 가격|||
|unitQuantity|Number|제품 단위 수량|||
|unit|String|제품 단위|||
|nowCount|Number|현재 모집 단위(내가 살 단위)|||
|totalCount|Number|총 모집 단위(내가 살 단위)|||
|url|String|상품 URL|||
|categoryId|Number|카테고리 ID|||
|keywords|Array|키워드|||
|images|Array|이미지|||
|expireTime|String|게시글 만료 시간|||
### 응답(#_응답_3)
HTTP/1.1 200 OK

---
## 게시글 수정
### 요청
PATCH /deal/{dealId} 

|**Parameter**|**Description**|**Optional**|**Constraint**|
| :- | :- | :- | :- |
|dealId|게시글 ID|||


|**Path**|**Type**|**Description**|**Optional**|**Constraint**|
| :- | :- | :- | :- | :- |
|content|String|변경 내용|Y||
|images|Array|변경 이미지|Y||
|keywords|Array|변경 키워드|Y||
### 응답
HTTP/1.1 200 OK

---
## 게시글 삭제
### 요청
DELETE /deal/{dealId} 


|**Parameter**|**Description**|**Optional**|**Constraint**|
| :- | :- | :- | :- |
|dealId|게시글 ID|||
### 응답

HTTP/1.1 200 OK  

---
## 구매 참여
### 요청
POST /deal/{dealId}/enrollment

|**Parameter**|**Description**|**Optional**|**Constraint**|
| :- | :- | :- | :- |
|dealId|게시글 ID|||


|**Path**|**Type**|**Description**|**Optional**|**Constraint**|
| :- | :- | :- | :- | :- |
|quantity|Number|구매 수량|||
### 응답
HTTP/1.1 200 OK  

---
## 구매 수정
### 요청
PATCH /deal/{dealId}/enrollment

|**Parameter**|**Description**|**Optional**|**Constraint**|
| :- | :- | :- | :- |
|dealId|게시글 ID|||


|**Path**|**Type**|**Description**|**Optional**|**Constraint**|
| :- | :- | :- | :- | :- |
|quantity|Number|구매 변경 수량|||
### 응답
HTTP/1.1 200 OK

---
## 구매 취소
### 요청
DELETE /deal/{dealId}/enrollment 

|**Parameter**|**Description**|**Optional**|**Constraint**|
| :- | :- | :- | :- |
|dealId|게시글 ID|||
### 응답
HTTP/1.1 200 OK

---
## 참여 내역 조회
### 요청
GET /deal/enrollment/{userId} 

|**Parameter**|**Description**|**Optional**|**Constraint**|
| :- | :- | :- | :- |
|userId|유저 ID|||
### 응답
HTTP/1.1 200 OK

|**Path**|**Type**|**Description**|
| :- | :- | :- |
|[].id|Number|게시글 ID|
|[].category|String|제품 카테고리|
|[].title|String|게시글 제목|
|[].remainDate|Number|남은 날짜|
|[].unitPrice|Number|단위 가격|
|[].quantity|Number|제품 수량|
|[].nowCount|Number|현재 모집 수량|
|[].totalCount|Number|총 모집 수량|
|[].image.fileName|String|이미지 이름|
|[].image.thumbnail|Boolean|썸네일 여부|
|[].deleted|Boolean|삭제 여부|
|[].expired|Boolean|만료 여부|
|[].userCount|Number|유저 구매 갯수|
|[].unit|String|단위 명|
|[].expiredDate|String|만료 일자|
|[].hostName|String|판매자 이름|
---
## 판매 내역 조회
### 요청
GET /deal/sale/{userId}
|**Parameter**|**Description**|**Optional**|**Constraint**|
| :- | :- | :- | :- |
|userId|유저 ID|||
### 응답
HTTP/1.1 200 OK

|**Path**|**Type**|**Description**|
| :- | :- | :- |
|[].id|Number|게시글 ID|
|[].category|String|제품 카테고리|
|[].title|String|게시글 제목|
|[].remainDate|Number|남은 날짜|
|[].unitPrice|Number|단위 가격|
|[].quantity|Number|제품 수량|
|[].nowCount|Number|현재 모집 수량|
|[].totalCount|Number|총 모집 수량|
|[].image.fileName|String|이미지 이름|
|[].image.thumbnail|Boolean|썸네일 여부|
|[].deleted|Boolean|삭제 여부|
|[].expired|Boolean|만료 여부|
|[].userCount|Number|유저 구매 갯수|
|[].unit|String|단위 명|
|[].expiredDate|String|만료 일자|
|[].hostName|String|판매자 이름|

