package com.tqmall.monkey.domain.entity;

import lombok.Data;

@Data
public class UserDO {
    private Integer id;
    private String userName;
    private String passWord;
    private String nickName;
    private String dept;
    private String company;
}