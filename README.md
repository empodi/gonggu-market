# 우리동네 공구마켓

마이크로서비스 아키텍쳐를 활용한 프로젝트  
프로젝트 기간 : 2022.09 ~ 2022.12  
프로젝트 인원 : 4인

|     이름     | 역할                                                           | 사용 기술                                     |
| :----------: | :------------------------------------------------------------- | :-------------------------------------------- |
|   jsc9988    | 회원가입, 로그인, 회원 관리 백엔드 개발 및 Spring dockerize    |                                               |
|   park-sy    | 공동구매모집,페이기능백엔드개발, Spring dockerize, DevOps(AWS) | Java11, Springboot, MySQL, Kafka            |
| JaeHyun Shin | 프론트엔드 개발, 푸시 서버 개발, PWA 구현, Mock server 구축    |                                               |
|    empodi    | 채팅 컨텍스트 백엔드 및 프론트엔드, Node.js dockerize          | Node.js, MySQL, Socket.io, Redis, Vue3, Kafka |

## 프로젝트 설명

쇼핑몰이나 대형마트에서 물품을 살 때 대량을 묶음으로 파는 제품들이 많은데, 혼자 사는 사람들은 이때문에 필요 이상으로 많은 물품을 사는 경우가 종종 발생한다. 우리동네 공구마캣은 공동 구매 어플리케이션으로, 근처 지역에서 공동구매를 같이 할 사람을 직접 모집하거나 참여할 수 있는 플랫폼을 제공한다.

## 프로젝트 목표

![image](https://user-images.githubusercontent.com/53611554/208305031-c51ac286-b863-44b9-9cdf-82f1d1b3e7d8.png)

다양한 기술 스택을 기반으로 팀원이 모였기 때문이 이를 제대로 활용할 수 있는 MSA를 기반으로 한 웹 서비스를 제작하며, 이외의 다양한 기술을 사용하여 프로젝트를 진행한다.

- Client : PWA를 활용한 서비스 제작
- Server : 대규모 트래픽 처리에 유연한 아키텍쳐 구성히여 각 서비스가 유동적으로 scale-in/out할 수 있는 서비스 완성

## 프로젝트 설계

### 서비스 구조도

![image](https://user-images.githubusercontent.com/53611554/208305634-99bc2260-82f6-48a9-bacd-143c04a3d7aa.png)
![image](https://user-images.githubusercontent.com/53611554/208305710-82666869-b796-4d6e-a0ad-96e41a7c61d6.png)

### 아키텍쳐

![image](https://user-images.githubusercontent.com/53611554/208305467-7f6af692-1ba1-402e-8048-1b1b5119ebc7.png)

- PWA에 유연한 Vue.js 사용
- 5개의 마이크로 서비스 구현
- Docker를 통한 이미지화 및 컨테이너 구성
- AWS ECR/ECS를 통한 컨테이너 오케스트레이션
- Elastic Load Balancer를 통한 API 라우팅
- JWT를 통한 인증체계 구성
- Redis를 활용한 Refresh Token 저장 및 Socket 관리
- Kafka를 통한 서버간 비동기 통신

### ERD

![image](https://user-images.githubusercontent.com/53611554/208304882-7a6db0a6-ba7b-4a96-9fe1-61366304ca7b.png)

## 프로젝트 결과

### 서비스 실행

#### PWA
![KakaoTalk_20221225_022555934](https://user-images.githubusercontent.com/53611554/209446052-7dffd0c9-e55b-4e12-99db-d15cbb98a9ee.gif)
![KakaoTalk_20221225_022619059](https://user-images.githubusercontent.com/53611554/209446055-243f078f-11ad-45ba-bea6-2117e9a340cf.gif)
![KakaoTalk_20221225_022636060_01](https://user-images.githubusercontent.com/53611554/209446059-82bd7a02-8e1e-487b-9ad7-735c2ae329e9.gif)


#### 로그인
![ezgif com-gif-maker](https://user-images.githubusercontent.com/53611554/209445242-dd33b67a-2aa6-4b08-a094-4f338e9eec1b.gif)

#### 게시글 생성
![ezgif com-gif-maker (1)](https://user-images.githubusercontent.com/53611554/209445247-9eabaa4a-8b36-42ac-999c-0cb554b2d265.gif)

#### 이미지 업로드
![ezgif com-gif-maker (2)](https://user-images.githubusercontent.com/53611554/209445250-b5138e13-8c96-40ea-be46-d613c9991eef.gif)

#### 공구 참여
![ezgif com-gif-maker (4)](https://user-images.githubusercontent.com/53611554/209445321-39764a96-de1e-4250-bfd9-20a27a89e4d9.gif)

#### 알림
![KakaoTalk_20221225_022636060](https://user-images.githubusercontent.com/53611554/209446068-c9a6c52e-a3a6-4ee7-9e6d-1631355c3c7f.gif)

#### 채팅
![KakaoTalk_20221225_015726667_07](https://user-images.githubusercontent.com/53611554/209445286-d3b08aff-58e0-4aa7-a7fe-e0d0d8fdd8ff.gif)

#### 송금
![ezgif com-gif-maker (3)](https://user-images.githubusercontent.com/53611554/209445296-4995c61a-f84e-42e8-9751-3802f01aebdf.gif)

#### 송금 내역
![KakaoTalk_20221225_015726667_04](https://user-images.githubusercontent.com/53611554/209445359-d68843e9-cd16-4e9e-b0ac-c75131029ea4.gif)
![KakaoTalk_20221225_015726667_05](https://user-images.githubusercontent.com/53611554/209445370-fc041b01-7399-4b2f-87b7-3062e53baa4b.gif)


#### PWA 검증

- 내용 추가 예정

### 서비스 검증

#### Back-End 검증

![image](https://user-images.githubusercontent.com/53611554/208305793-bb293b1f-10b9-424a-a72e-5e418111d7c8.png)

- 검증 목표는 무중단 배포, 확장성, 독립성 세 가지로 설정
- 무중단 배포와, 확장성을 검증하기 위해 부하 테스트 진행
- 테스트 시작과 동시에 서버 업데이트를 진행하였고 02:42에 서버 업데이트가 완료되며 무중단 배포 성공
- 이후 scale-out 성공하여 서버 3대까지 증가
- 독립성 내용 추가 예정
