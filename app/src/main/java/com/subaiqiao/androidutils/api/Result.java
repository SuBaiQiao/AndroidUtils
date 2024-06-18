package com.subaiqiao.androidutils.api;

import java.io.Serializable;

public class Result implements Serializable {
    private Object message;
    private String result;
    public String getResult() {
        return this.result;
    }
    public void setResult(String result) {
        this.result = result;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
