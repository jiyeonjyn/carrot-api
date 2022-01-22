package com.umc.courtking.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PostProductReq {
    private int userIdx;
    private int categoryIdx;
    private String productName;
    private int price;
    private String productDetail;
    private String productMainImg;
}
