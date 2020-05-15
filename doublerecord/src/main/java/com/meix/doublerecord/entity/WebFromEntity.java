package com.meix.doublerecord.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * web页面传过来的参数
 */
public class WebFromEntity implements Parcelable {
    private String flowId;
    private String productId;
    private String userCode;
    private int recordingType;

    protected WebFromEntity(Parcel in) {
        flowId = in.readString();
        productId = in.readString();
        userCode = in.readString();
        recordingType = in.readInt();
    }

    public static final Creator<WebFromEntity> CREATOR = new Creator<WebFromEntity>() {
        @Override
        public WebFromEntity createFromParcel(Parcel in) {
            return new WebFromEntity(in);
        }

        @Override
        public WebFromEntity[] newArray(int size) {
            return new WebFromEntity[size];
        }
    };

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public int getRecordingType() {
        return recordingType;
    }

    public void setRecordingType(int recordingType) {
        this.recordingType = recordingType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(flowId);
        dest.writeString(userCode);
        dest.writeString(productId);
        dest.writeInt(recordingType);
    }
}
