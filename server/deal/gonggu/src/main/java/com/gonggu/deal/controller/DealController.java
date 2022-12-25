package com.gonggu.deal.controller;

import com.gonggu.deal.domain.User;
import com.gonggu.deal.request.*;
import com.gonggu.deal.response.DealDetailResponse;
import com.gonggu.deal.response.DealMemberResponse;
import com.gonggu.deal.response.DealResponse;
import com.gonggu.deal.service.DealService;
import com.gonggu.deal.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@CrossOrigin(origins = "http://localhost:8080",allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;
    private final KafkaProducer kafkaProducer;
    //게시글 불러오기
    @GetMapping("/deal")
    public List<DealResponse> getDeal(@ModelAttribute DealSearch dealSearch){
        return dealService.getList(dealSearch);
    }
    //게시글 상세 보기
    @GetMapping("/deal/{dealId}")
    public DealDetailResponse getDealDetail(@PathVariable Long dealId){
        dealService.updateView(dealId);
        return dealService.getDealDetail(dealId);
    }
    //게시글 작성
    @PostMapping("/deal")
    public void postDeal(@AuthenticationPrincipal User user,@RequestBody DealCreate dealCreate){
        dealService.createDeal(dealCreate, user);
    }
   //게시글 수정
    @PatchMapping("/deal/{dealId}")
    public void editDeal(@PathVariable Long dealId, @RequestBody DealEdit dealEdit){
        dealService.editDeal(dealId,dealEdit);
    }
    //게시글 삭제
    @DeleteMapping("/deal/{dealId}")
    public void deleteDeal(@PathVariable Long dealId){
        dealService.deleteDeal(dealId);
        kafkaProducer.sendDealMemberToPush("dealDelete",dealId);
    }
    //구매 참가 요청
    @PostMapping("/deal/{dealId}/enrollment")
    public void requestJoin(@PathVariable Long dealId, @AuthenticationPrincipal User user,
                            @RequestBody DealJoin join){
        if(dealService.createJoin(dealId, join, user)) kafkaProducer.sendDealMemberToPush("dealComplete",dealId);
        else kafkaProducer.sendDealMemberToPush("dealJoin",dealId);
        kafkaProducer.sendDealAndUserToChat("chatJoin",dealId,user);
    }

    //구매 정보 수정
    @PatchMapping("/deal/{dealId}/enrollment")
    public void editJoin(@PathVariable Long dealId, @AuthenticationPrincipal User user,
                         @RequestBody DealJoin join){
        if(dealService.editJoin(dealId,join,user)) kafkaProducer.sendDealMemberToPush("dealComplete",dealId);;
    }
    //구매 철회
    @DeleteMapping("/deal/{dealId}/enrollment")
    public void deleteJoin(@PathVariable Long dealId, @AuthenticationPrincipal User user){
        dealService.deleteJoin(dealId,user);
        kafkaProducer.sendDealAndUserToChat("chatExit",dealId,user);
    }
    //구매자 명단 가져오기
    @GetMapping("/deal/{dealId}/enrollment")
    public List<DealMemberResponse> getJoin(@PathVariable Long dealId){
        return dealService.getJoin(dealId);
    }

    //내 판매 내역
    @GetMapping("/deal/sale/{userId}")
    public List<DealResponse> getMySellDeal(@PathVariable String userId){
        return dealService.getSellDeal(userId);
    }
    //내 구매 내역
    @GetMapping("/deal/enrollment/{userId}")
    public List<DealResponse> getMyJoinDeal(@PathVariable String userId){
        return dealService.getJoinDeal(userId);
    }
    @GetMapping("/health")
    public String getHealth2(){
        return "good";
    }
    @GetMapping("/user")
    public User userinfo(@AuthenticationPrincipal User user){
        return user;
    }
    //참여 시 채팅방 서버에 request
    @GetMapping("/deal/time")
    public String getTime(){
        LocalDateTime now = LocalDateTime.now().plusHours(9);
        return now.toString();
    }
}
