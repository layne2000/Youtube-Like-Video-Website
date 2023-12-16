package org.example.util;

import java.io.Serializable;

public class JsonResponse<T> implements Serializable {
    private String code;
    private String msg;
    private T data;

    public JsonResponse(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonResponse(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public JsonResponse(T data){
        code = "0";
        msg = "success";
        this.data = data;
    }

    public static JsonResponse<String> success(){
        return new JsonResponse<String>("0", "success");
    }

    // <E> ahead is required
    public static <E> JsonResponse<E> success(E data){
        return new JsonResponse<E>("0", "success", data);
    }

    public static JsonResponse<String> failure(){
        return new JsonResponse<>("1", "failure");
    }

    public static JsonResponse<String> failure(String code, String msg){
        return new JsonResponse<>(code, msg);
    }

    public String getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }

    public T getDate(){
        return data;
    }

    public void setCode(String code){
        this.code = code;
    }
    
    public void setMsg(String msg){
        this.msg = msg;
    }

    public void setData(T data){
        this.data = data;
    }

    @Override
    public String toString() {
        return "JsonResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
