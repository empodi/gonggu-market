# 공구마켓 서버 
## Rule
### Docker Image Tag
- 이미지 빌드 형식 : {service}:{tag}
- tag 형식: yymmddhhmm (ex 2211231722)

### Kafka Topic Rule
- from Deal to Push
    - `dealJoin` : 공구 참여
        - { “dealId”: 56, “title”:”테스트방”, “nickname”:["테스트유저1","테스트유저2"] }
        - {"dealId":7,"title":"이번엔 제발 성공....","nickname":["nick1","nick2","psy"]}
    - `dealComplete` : 공구 완료
        - { “dealId”: 56, “title”:”테스트방”, “nickname”:["테스트유저1","테스트유저2"] }
    - `dealDelete` : 공구 삭제
        - { “dealId”: 56, “title”:”테스트방”, “nickname”:["테스트유저1","테스트유저2"] }
- from Deal to Chat
    - `chatJoin` : 채팅 참여(공구 참여)
        - {"dealId":56, “title”:”테스트방”, "nickName":"테스트유저"}
    - `chatExit` : 채팅 나감(공구 취소)
        - {"dealId":56, “title”:”테스트방”, "nickName":"테스트유저"}
- from Chat to Chat
    - `chatMessage` : 메시지 전송/수신
        - { “dealId”:56, ”nickName”:”테스트유저”, “dealTitle”:”테스트방”}
- from Payment to Push
    - `remitCreate` : 송금 발생
        - {"remitCreate":{"nickName":"테스트유저","amount":30000,"remitTime":"2022-12-11T23:42:57"}}
        


    
