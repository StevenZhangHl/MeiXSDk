package com.meix.doublerecord.mvp.model;

import com.meix.doublerecord.api.ApiManager;
import com.meix.doublerecord.entity.BaseEntity;
import com.meix.doublerecord.entity.BaseFaceRecognitionEntity;
import com.meix.doublerecord.entity.QuestionEntity;
import com.meix.doublerecord.entity.UserInfoEntity;
import com.meix.doublerecord.mvp.contract.UserInfoContract;
import com.meix.doublerecord.rx.RxHelper;

import java.util.Map;

import io.reactivex.Observable;

public class UserInfoModel implements UserInfoContract.Model {

    @Override
    public Observable<UserInfoEntity> getUserInfo(Map<String, Object> objectMap) {
        return ApiManager.getInstance().getUserInfo(objectMap).compose(RxHelper.<UserInfoEntity>getResult());
    }

    @Override
    public Observable<QuestionEntity> updateVoiceQuestions(Map<String, Object> objectMap) {
        return ApiManager.getInstance().updateVoiceQuestions(objectMap).compose(RxHelper.<QuestionEntity>getResult());
    }

    @Override
    public Observable<String> getPushUrl(Map<String, Object> objectMap) {
        return ApiManager.getInstance().getPushUrl(objectMap).compose(RxHelper.<String>getResult());
    }

    @Override
    public Observable<String> sendRecordStreamRequest(Map<String, Object> objectMap) {
        return ApiManager.getInstance().sendRecordStreamRequest(objectMap).compose(RxHelper.<String>getResult());
    }

    @Override
    public Observable<QuestionEntity> checkAnswer(Map<String, Object> objectMap) {
        return ApiManager.getInstance().checkAnswer(objectMap).compose(RxHelper.<QuestionEntity>getResult());
    }

    @Override
    public Observable<BaseFaceRecognitionEntity> detectLiveFace(Map<String, Object> objectMap) {
        return ApiManager.getInstance().detectLiveFace(objectMap).compose(RxHelper.<BaseFaceRecognitionEntity>customResult());
    }

    @Override
    public Observable<BaseEntity> saveVideo(Map<String, Object> objectMap) {
        return ApiManager.getInstance().saveVideo(objectMap).compose(RxHelper.<BaseEntity>customResult());
    }
}
