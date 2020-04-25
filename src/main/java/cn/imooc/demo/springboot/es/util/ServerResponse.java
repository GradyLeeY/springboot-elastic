package cn.imooc.demo.springboot.es.util;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {

    private String msg;

    private ServerResponse(String msg){
        this.msg = msg;
    }

    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<>("没有该查询方式，请选择页面提供的两类方式进行查询");
    }
}
