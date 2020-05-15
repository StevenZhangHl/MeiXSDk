package com.meix.doublerecord.entity;

public class BaseFaceRecognitionEntity {

    /**
     * pic_full_url : in culpa ut
     * order_no : deserunt magna laborum ipsum
     * msg : ipsum magna veniam
     * birthday : eu reprehenderit occaecat
     * score : -1.6534674565118834E7
     * address : aliqua
     * incorrect : 6318731.204975143
     * sex : labore ullamco
     */
    private String message;
    private int code;
    private boolean data;
    private FaceRecognitionEntity jsonObject;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    public FaceRecognitionEntity getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(FaceRecognitionEntity jsonObject) {
        this.jsonObject = jsonObject;
    }
}
