package com.meix.doublerecord.api;


import com.meix.doublerecord.entity.BaseEntity;
import com.meix.doublerecord.entity.BaseFaceRecognitionEntity;
import com.meix.doublerecord.entity.QuestionEntity;
import com.meix.doublerecord.entity.UserInfoEntity;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * @user steven
 * @createDate 2019/2/20 13:37
 * @description 接口
 */
public interface Api {
    /**
     * 获取用户信息
     *
     * @param map
     * @return
     */
    @POST("manager/app/queryUserInfo")
    Observable<BaseEntity<UserInfoEntity>> getUserInfo(@QueryMap Map<String, Object> map);

    /**
     * 初始化题目
     * @param map
     * @return
     */
    @POST("manager/miniProgram/updateVoiceQuestions")
    Observable<BaseEntity<QuestionEntity>> updateVoiceQuestions(@QueryMap Map<String, Object> map);

    /**
     * 检查题目
     * @param map
     * @return
     */
    @POST("manager/app/checkAnswer")
    Observable<BaseEntity<QuestionEntity>> checkAnswer(@QueryMap Map<String, Object> map);

    /**
     * 腾讯云推流地址生成
     * @param map
     * @return
     */
    @POST("manager/app/getPushUrl")
    Observable<BaseEntity<String>> getPushUrl(@QueryMap Map<String, Object> map);

    /**
     * 截取音频
     * @param map
     * @return
     */
    @POST("manager/app/sendRecordStreamRequest")
    Observable<BaseEntity<String>> sendRecordStreamRequest(@QueryMap Map<String, Object> map);

    /**
     * 人脸校验
     * @param map
     * @return
     */
    @POST("manager/app/detectLiveFace")
    Observable<BaseFaceRecognitionEntity> detectLiveFace(@QueryMap Map<String, Object> map);

    /**
     * 双录保存
     * @param map
     * @return
     */
    @POST("sign/flow/save")
    Observable<BaseEntity> saveVideo(@QueryMap Map<String, Object> map);
}
