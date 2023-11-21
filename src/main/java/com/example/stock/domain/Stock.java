package com.example.stock.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long itemId;
    private Long quantity;

    @Builder
    public Stock(Long itemId, Long quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public void decrease(Long decreaseQuantity) {
        if(this.quantity - decreaseQuantity < 0) {
            throw new RuntimeException("재고는 0 미만일 수 없습니다.");
        }

        this.quantity -= decreaseQuantity;
    }
}
