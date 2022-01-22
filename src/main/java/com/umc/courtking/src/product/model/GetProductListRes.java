package com.umc.courtking.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProductListRes {
    private int productIdx;
    private int userIdx;
    private int categoryIdx;
    private String productName;
    private int price;
    private String productMainImg;
    private String status;
    private String updateAt;
    private String nickName;
    private String address;
}
