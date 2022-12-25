package com.gonggu.deal.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gonggu.deal.domain.Deal;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DealResponse {

    private final Long id;
    private final String category;
    private final String title;
    private final Long remainDate;
    private final Long unitPrice;
    private final Integer quantity;
    private final Integer nowCount;
    private final Integer totalCount;
    private final DealImageResponse image;
    private final boolean deleted;
    private final boolean expired;
    private Integer userCount;
    private String unit;
    private LocalDateTime expiredDate;
    private String hostName;

    public DealResponse(Deal deal){
        LocalDateTime now = LocalDateTime.now().plusHours(9);
        this.id = deal.getId();
        this.category = deal.getCategory().getName();
        this.title = deal.getTitle();
        this.remainDate = ChronoUnit.DAYS
                .between(now.toLocalDate(),deal.getExpireTime().toLocalDate());
        this.unitPrice = deal.getUnitPrice();
        this.quantity = deal.getQuantity();
        this.nowCount = deal.getNowCount();
        this.totalCount = deal.getTotalCount();
        this.image = new DealImageResponse(deal.getImages().get(0));
        this.deleted = deal.isDeletion();
        this.expired = deal.getExpireTime().toLocalDate().isBefore(now.toLocalDate());
    }

    public DealResponse(Deal deal, Integer userCount){
        LocalDateTime now = LocalDateTime.now().plusHours(9);
        this.id = deal.getId();
        this.category = deal.getCategory().getName();
        this.title = deal.getTitle();
        this.remainDate = ChronoUnit.DAYS
                .between(now.toLocalDate(),deal.getExpireTime().toLocalDate());
        this.unitPrice = deal.getUnitPrice();
        this.quantity = deal.getQuantity();
        this.nowCount = deal.getNowCount();
        this.totalCount = deal.getTotalCount();
        this.image = new DealImageResponse(deal.getImages().get(0));
        this.deleted = deal.isDeletion();
        this.expired = deal.getExpireTime().toLocalDate().isBefore(now.toLocalDate());
        this.userCount = userCount;
        this.unit = deal.getUnit();
        this.expiredDate = deal.getExpireTime();
        this.hostName = deal.getUser().getNickname();
    }
}
