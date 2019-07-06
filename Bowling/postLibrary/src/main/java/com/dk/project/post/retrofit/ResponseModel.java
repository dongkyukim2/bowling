package com.dk.project.post.retrofit;

public class ResponseModel<T> {

    private String code;
    private T data;
    private String message;

    public boolean isSuccess() {
        return code.equals("0000");
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
