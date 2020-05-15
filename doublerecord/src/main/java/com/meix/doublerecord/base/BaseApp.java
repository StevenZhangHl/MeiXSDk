package com.meix.doublerecord.base;

import android.content.res.Resources;

import androidx.multidex.MultiDexApplication;

import com.meix.doublerecord.net.Url;
import com.tencent.rtmp.TXLiveBase;


/**
 * @user steven
 * @createDate 2019/1/23 10:39
 * @description 自定义
 */
public class BaseApp extends MultiDexApplication {
    public static BaseApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        String licenceURL = "http://license.vod2.myqcloud.com/license/v1/b278a1ba486c14542421fd8510ffc191/TXLiveSDK.licence"; // 获取到的 licence url
        String licenceKey = "99abe8e04f72ffce96c4bff849f75201"; // 获取到的 licence key
        TXLiveBase.getInstance().setLicence(this, licenceURL, licenceKey);
    }

    public static BaseApp getInstance() {
        return instance;
    }

    public static Resources getAppResources() {
        return instance.getResources();
    }
}
