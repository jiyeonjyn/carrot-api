package com.umc.courtking.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PutProductReq {
    private int userIdx;
    private int productIdx;
    private int categoryIdx;
    private String productName;
    private int price;
    private String productDetail;
    private String productMainImg;
}