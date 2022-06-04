package com.example.androidforecommerce.pojo;

import java.io.Serializable;

//用来封装从服务器端拿到的数据

public class SverResponse<T> implements Serializable {
    private int status;
    private String name;
    private String msg;
    private T data;

    public SverResponse(){

    }
    public SverResponse(int status, String name, T data){
        this.status=status;
        this.name=name;
        this.data=data;
    }

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
