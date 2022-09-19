package com.example.helloworld.pojo;

import java.util.List;

public class UploadResponse {
    public String cat;
    public String msg;
    public List<Integer> size;

    public UploadResponse(String cat, String msg, List<Integer> size) {
        this.cat = cat;
        this.msg = msg;
        this.size = size;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Integer> getSize() {
        return size;
    }

    public void setSize(List<Integer> size) {
        this.size = size;
    }

}
