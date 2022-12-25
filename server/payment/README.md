# 공구마켓 Payment 서버 API
---
## Table of Contents  
|지갑 정보 관련|거래 관련|
|------|---|
|[지갑 정보 가져오기](#지갑-정보-가져오기)  |[지갑 코인 충전](#지갑-코인-충전)      |
|[지갑 정보 가져오기 실패](#지갑-정보-가져오기-실패)    |[지갑 충전 반환](#지갑-충전-반환)     |
|[지갑 만들기](#지갑-만들기) |[송금](#송금) |
| |[거래내역 가져오기](#거래내역-가져오기)     |

---



## 지갑 정보 가져오기
GET /payment 


### 응답
HTTP/1.1 200 OK


|**Path**|**Type**|**Description**|
| :- | :- | :- |
|walletId|Number|지갑 ID|
|balance|Number|지갑 잔액|
---
## 지갑 정보 가져오기 실패
GET /payment HTTP/1.1

### 응답
HTTP/1.1 404 Not Found


|**Path**|**Type**|**Description**|
| :- | :- | :- |
|code|String|에러 코드|
|message|String|에러 메세지|
|validation|Object|validation|
---
## 지갑 만들기
POST /payment 

### 응답
HTTP/1.1 200 OK

---
## 지갑 코인 충전
POST /payment/charge 
### 응답
HTTP/1.1 200 OK


---
## 지갑 충전 반환
POST /payment/charge 

|**Path**|**Type**|**Description**|**Optional**|**Constraint**|
| :- | :- | :- | :- | :- |
|account|String|계좌 이름|||
|requestCoin|Number|충전 금액|||
### 응답
HTTP/1.1 200 OK


---
## 송금
POST /payment/remit 
|**Path**|**Type**|**Description**|**Optional**|**Constraint**|
| :- | :- | :- | :- | :- |
|to|String|송금 상대 유저 ID|||
|amount|Number|송금 금액|||
### 응답
HTTP/1.1 200 OK

---
## 거래내역 가져오기
GET /payment/transaction?filter=1&order=1&start=2022-12-08T23:44:49.386605300&end=2022-12-11T23:44:49.386605300 

|**Parameter**|**Description**|**Optional**|**Constraint**|
| :- | :- | :- | :- |
|filter|송금 상대 유저 ID|Y|입력 x: 모두, 1:보낸 내역, 2: 받은 내역|
|order|순서|Y|입력 x: 최신순, 1: 금액 순|
|start|검색 범위 종료 날짜|Y|LocalDateTime 형식, 입력하지 않으면 현시간까지|
### 응답
HTTP/1.1 200 OK

|**Path**|**Type**|**Description**|
| :- | :- | :- |
|[].id|Number|거래내역 ID|
|[].fromName|String|송금자 이름|
|[].toName|String|수신자 이름|
|[].amount|Number|거래 금액|
|[].time|String|거래 시간|


