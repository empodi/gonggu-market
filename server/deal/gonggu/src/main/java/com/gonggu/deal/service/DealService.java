package com.gonggu.deal.service;

import com.gonggu.deal.domain.*;
import com.gonggu.deal.exception.CategoryNotFound;
import com.gonggu.deal.exception.DealJoinFailed;
import com.gonggu.deal.exception.DealNotFound;
import com.gonggu.deal.exception.UserNotFound;
import com.gonggu.deal.repository.*;
import com.gonggu.deal.request.*;
import com.gonggu.deal.response.DealDetailResponse;
import com.gonggu.deal.response.DealMemberResponse;
import com.gonggu.deal.response.DealResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DealService {

    private final DealRepository dealRepository;
    private final DealKeywordRepository dealKeywordRepository;
    private final DealImageRepository dealImageRepository;
    private final DealMemberRepository dealMemberRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final KeywordRepository keywordRepository;



    public List<DealResponse> getList(DealSearch dealSearch) {

        return dealRepository.getList(dealSearch).stream()
                .map(DealResponse::new).collect(Collectors.toList());
    }
    public DealDetailResponse getDealDetail(Long id){
        Deal deal = dealRepository.findById(id).orElseThrow(DealNotFound::new);
        DealDetailResponse dealDetailResponse = new DealDetailResponse(deal);
        return dealDetailResponse;
    }

    public void createDeal(DealCreate dealCreate, User user){
        Category category = categoryRepository.findById(dealCreate.getCategoryId()).orElseThrow(CategoryNotFound::new);
        Deal deal = Deal.builder()
                .title(dealCreate.getTitle())
                .content(dealCreate.getContent())
                .price(dealCreate.getPrice())
                .unitPrice(dealCreate.getPrice()/dealCreate.getUnitQuantity())
                .quantity(dealCreate.getUnitQuantity()*dealCreate.getTotalCount())
                .unitQuantity(dealCreate.getUnitQuantity())
                .unit(dealCreate.getUnit())
                .url(dealCreate.getUrl())
                .nowCount(dealCreate.getNowCount())
                .user(user)
                .createTime(LocalDateTime.now())
                .expireTime(dealCreate.getExpireTime().plusHours(9))
                .totalCount(dealCreate.getTotalCount())
                .category(category)
                .build();
        dealRepository.save(deal);

        DealMember dealMember = DealMember.builder()
                .deal(deal)
                .user(user)
                .host(true)
                .quantity(dealCreate.getNowCount())
                .build();
        dealMemberRepository.save(dealMember);

        if(dealCreate.getKeywords() != null){
            for(String keyword : dealCreate.getKeywords()){
                Keyword findKey = keywordRepository.findByWord(keyword);
                if (findKey == null) {
                    findKey = Keyword.builder().word(keyword).build();
                    keywordRepository.save(findKey);
                }
                DealKeyword bk = DealKeyword.builder().keyword(findKey).deal(deal).build();
                dealKeywordRepository.save(bk);
            }
        }
        if(dealCreate.getImages() != null){
            List<DealImage> dealImages = dealCreate.getImages().stream()
                    .map(path-> new DealImage(deal,path)).collect(Collectors.toList());
            dealImages.get(0).setThumbnail(true);
            dealImageRepository.saveAll(dealImages);
        }
    }
    public void deleteDeal(Long id){
        Deal deal = dealRepository.findById(id).orElseThrow(DealNotFound::new);
        DealEditor.DealEditorBuilder editorBuilder = deal.toEditor();
        DealEditor dealEditor = editorBuilder.deletion(true).build();
        deal.edit(dealEditor);
    }
    public DealDetailResponse editDeal(Long id, DealEdit dealEdit){
        Deal deal = dealRepository.findById(id).orElseThrow(DealNotFound::new);
        DealEditor.DealEditorBuilder editorBuilder = deal.toEditor();
        DealEditor dealEditor = editorBuilder.content(dealEdit.getContent()).build();
        deal.edit(dealEditor);

        if (dealEdit.getKeywords()!=null){
            List<DealKeyword> deleteKeyword = dealKeywordRepository.findByDeal(deal);
            dealKeywordRepository.deleteAll(deleteKeyword);
            for(String keyword : dealEdit.getKeywords()){
                Keyword findKey = keywordRepository.findByWord(keyword);
                if (findKey == null) {
                    findKey = Keyword.builder().word(keyword).build();
                    keywordRepository.save(findKey);
                }
                DealKeyword bk = DealKeyword.builder().keyword(findKey).deal(deal).build();
                dealKeywordRepository.save(bk);
            }
        }
        if(dealEdit.getImages()!=null){
            List<DealImage> deleteImage = dealImageRepository.findByDeal(deal);
            dealImageRepository.deleteAll(deleteImage);
            List<DealImage> dealImages = dealEdit.getImages().stream()
                    .map(path-> new DealImage(deal,path)).collect(Collectors.toList());
            dealImages.get(0).setThumbnail(true);
            dealImageRepository.saveAll(dealImages);
        }
        DealDetailResponse dealDetailResponse = new DealDetailResponse(deal);
        return dealDetailResponse;
    }

    public void updateView(Long dealId) {
        dealRepository.updateView(dealId);
    }

    public boolean createJoin(Long dealId, DealJoin join, User user) {
        Deal deal = dealRepository.findById(dealId).orElseThrow(DealNotFound::new);
        if(dealMemberRepository.findByDealAndUser(deal,user).isPresent()) throw new DealJoinFailed("이미 참여한 공구입니다.");
        if(deal.getNowCount() + join.getQuantity() > deal.getTotalCount()) throw new DealJoinFailed("구매 참여에 실패하였습니다.");

        deal.editCount(join.getQuantity() + deal.getNowCount());
        DealMember dealMember = DealMember.builder()
                .deal(deal)
                .user(user)
                .quantity(join.getQuantity())
                .build();
        dealMemberRepository.save(dealMember);
        if(deal.getNowCount() == deal.getTotalCount()) return true;
        else return false;
    }

    public boolean editJoin(Long dealId, DealJoin join, User user){
        Deal deal = dealRepository.findById(dealId).orElseThrow(DealNotFound::new);
        DealMember dealMember = dealMemberRepository.findByDealAndUser(deal, user).orElseThrow(UserNotFound::new);
        Integer afterCount = deal.getNowCount() + join.getQuantity() - dealMember.getQuantity();
        if(afterCount < 0 || afterCount > deal.getTotalCount()) throw new DealJoinFailed("수량 변경에 실패하였습니다.");

        deal.editCount(afterCount);
        dealMember.editQuantity(join.getQuantity());
        if(afterCount == deal.getTotalCount()) return true;
        else return false;
    }

    public void deleteJoin(Long dealId, User user){
        Deal deal = dealRepository.findById(dealId).orElseThrow(DealNotFound::new);
        DealMember dealMember = dealMemberRepository.findByDealAndUser(deal, user).orElseThrow(UserNotFound::new);
        deal.editCount(deal.getNowCount()-dealMember.getQuantity());
        dealMemberRepository.delete(dealMember);
    }

    public List<DealMemberResponse> getJoin(Long dealId){
        Deal deal = dealRepository.findById(dealId).orElseThrow(DealNotFound::new);
       return dealMemberRepository.findByDeal(deal).stream()
               .map(DealMemberResponse::new).collect(Collectors.toList());
    }

    public List<DealResponse> getSellDeal(String userId) {
        User user = userRepository.findByNickname(userId).orElseThrow(UserNotFound::new);
        return dealMemberRepository.getByUser(user,true).stream()
                .map(o-> new DealResponse(o.getDeal(), o.getQuantity())).collect(Collectors.toList());
    }

    public List<DealResponse> getJoinDeal(String userId){
        User user = userRepository.findByNickname(userId).orElseThrow(UserNotFound::new);
        return dealMemberRepository.getByUser(user,false).stream()
                .map(o-> new DealResponse(o.getDeal(), o.getQuantity())).collect(Collectors.toList());
    }
}
