package com.umc.courtking.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PostUserReq {
    private String nickName;
    private String phone;
    private String email;
    private String address;
    private String profileImg;
}
