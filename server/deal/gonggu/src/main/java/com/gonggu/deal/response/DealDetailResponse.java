package com.gonggu.deal.response;

import com.gonggu.deal.domain.Deal;
import com.gonggu.deal.domain.Category;
import com.gonggu.deal.domain.DealKeyword;
import com.gonggu.deal.domain.Keyword;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
@Getter
public class DealDetailResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final Long price;
    private final Long unitPrice;
    private final Long remainDate;
    private final Integer quantity;
    private final Integer unitQuantity;
    private final String unit;
    private final Integer nowCount;
    private final Integer totalCount;
    private final String url;
    private final List<DealImageResponse> images;
    private final List<String> keywords;
    private final int view;
    private final String user;
    private final Category category;
    private final LocalDateTime expiredDate;
    private final boolean deleted;
    private final boolean expired;

    public DealDetailResponse(Deal deal){
        LocalDateTime now = LocalDateTime.now().plusHours(9);
        this.id = deal.getId();
        this.title = deal.getTitle();
        this.content = deal.getContent();
        this.remainDate = ChronoUnit.DAYS
                .between(now.toLocalDate(),deal.getExpireTime().toLocalDate());
        this.price = deal.getPrice();
        this.unitPrice = deal.getUnitPrice();
        this.quantity = deal.getQuantity();
        this.unitQuantity = deal.getUnitQuantity();
        this.unit = deal.getUnit();
        this.nowCount = deal.getNowCount();
        this.totalCount = deal.getTotalCount();
        this.url = deal.getUrl();
        this.view = deal.getView();
        this.images = deal.getImages().stream().map(DealImageResponse::new).collect(Collectors.toList());
        this.keywords = deal.getKeywords().stream().map(o ->o.getKeyword().getWord()).collect(Collectors.toList());
        this.user = deal.getUser().getNickname();
        this.category = deal.getCategory();
        this.expiredDate = deal.getExpireTime();
        this.deleted = deal.isDeletion();
        this.expired = deal.getExpireTime().toLocalDate().isBefore(now.toLocalDate());

    }
}
