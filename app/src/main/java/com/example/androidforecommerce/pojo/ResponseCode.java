package com.example.androidforecommerce.pojo;
/*
* 定义响应码status的枚举类型
* */
public enum ResponseCode {
    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR")
    ;
    private final int code;
    private final String desc;//描述

    private ResponseCode(int code,String desc){
        this.code=code;
        this.desc=desc;
    }
    public int getCode(){
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
