package com.meix.doublerecord.entity;

public class FaceRecognitionEntity {
    private String pic_full_url;//人脸图片
    private String order_no;
    private String msg;
    private String birthday;
    private double score;//相似度
    private String address;
    private double incorrect;
    private String sex;

    public String getPic_full_url() {
        return pic_full_url;
    }

    public void setPic_full_url(String pic_full_url) {
        this.pic_full_url = pic_full_url;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(double incorrect) {
        this.incorrect = incorrect;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
