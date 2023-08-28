package com.ll.jigumiyak.product_category;

import com.ll.jigumiyak.base.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
public class ProductCategory extends BaseEntity {

}
// 약 / 영양제
// 약     { 신체부위 / 성분 / 종류 / 이름 / 가격 / 수량 / 효능 }
// 영양제 { 신체부위 / 성분 / 종류 / 이름 / 가격 / 수량 / 효능 }
// ㅇㅎ