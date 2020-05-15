package com.meix.doublerecord.entity;

public class UserInfoEntity {
    /**
     * 身份证号
     */
    private String operatorIdCard;
    /**
     * 产品id
     */
    private int productId;
    /**
     * 姓名
     */
    private String operatorName;
    /**
     * 证件类型
     */
    private String operatorIdCardType;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 用户id
     */
    private String userCode;
    /**
     * 双录唯一id很重要
     */
    private String unioncode;

    public String getOperatorIdCard() {
        return operatorIdCard;
    }

    public void setOperatorIdCard(String operatorIdCard) {
        this.operatorIdCard = operatorIdCard;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorIdCardType() {
        return operatorIdCardType;
    }

    public void setOperatorIdCardType(String operatorIdCardType) {
        this.operatorIdCardType = operatorIdCardType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUnioncode() {
        return unioncode;
    }

    public void setUnioncode(String unioncode) {
        this.unioncode = unioncode;
    }
}
