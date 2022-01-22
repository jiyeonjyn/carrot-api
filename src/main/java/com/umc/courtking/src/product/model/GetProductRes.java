package com.umc.courtking.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProductRes {
    private int productIdx;
    private int userIdx;
    private int categoryIdx;
    private String productName;
    private int price;
    private String productMainImg;
    private String productDetail;
    private String status;
    private String updateAt;
    private String nickName;
    private String address;
    private String category;
    private int hits;
}
