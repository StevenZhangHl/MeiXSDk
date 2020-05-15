package com.meix.doublerecord.entity;

public class QuestionEntity {
    /**
     * 问题内容
     */
    private String questionContet;
    /**
     * 问题id
     */
    private int questionId;
    /**
     * 回答时间
     */
    private int answerTime;
    private String idCard;
    /**
     * 总题
     */
    private int total;
    /**
     * 当前题
     */
    private int current;
    /**
     * 题目音频
     */
    private String fileUrl;
    private String voiceText;
    private String status;

    public String getQuestionContet() {
        return questionContet;
    }

    public void setQuestionContet(String questionContet) {
        this.questionContet = questionContet;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(int answerTime) {
        this.answerTime = answerTime;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getVoiceText() {
        return voiceText;
    }

    public void setVoiceText(String voiceText) {
        this.voiceText = voiceText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
