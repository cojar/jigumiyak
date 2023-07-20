package com.ll.jigumiyak.product_category;

import com.ll.jigumiyak.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
// 약 / 영양제
// 약     { 신체부위 / 성분 / 종류 / 이름 / 가격 / 수량 / 효능 }
// 영양제 { 신체부위 / 성분 / 종류 / 이름 / 가격 / 수량 / 효능 }
// ㅇㅎ