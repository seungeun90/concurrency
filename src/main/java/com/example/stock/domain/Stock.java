package com.example.stock.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Stock {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private Long quantity;

    @Version
    private Long version;

    public Stock(Long productId, Long quantity){
        this.productId = productId;
        this.quantity =quantity;
    }
    public void decrease(Long quantity){
        if(this.quantity - quantity < 0){
            throw new RuntimeException("재고가 부족합니다,");
        }

        this.quantity = this.quantity-quantity;
    }
}
