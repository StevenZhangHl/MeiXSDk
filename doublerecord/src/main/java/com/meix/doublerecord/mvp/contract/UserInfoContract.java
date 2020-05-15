package com.meix.doublerecord.mvp.contract;

import com.meix.doublerecord.base.BaseModel;
import com.meix.doublerecord.base.BasePresenter;
import com.meix.doublerecord.base.BaseView;
import com.meix.doublerecord.entity.BaseEntity;
import com.meix.doublerecord.entity.BaseFaceRecognitionEntity;
import com.meix.doublerecord.entity.FaceRecognitionEntity;
import com.meix.doublerecord.entity.QuestionEntity;
import com.meix.doublerecord.entity.UserInfoEntity;

import java.util.Map;

import io.reactivex.Observable;

public interface UserInfoContract {
    interface View extends BaseView {
        void setUserInfo(UserInfoEntity userInfo);

        void setQuestionInfo(QuestionEntity questionInfo);

        void setQuestionInfoStr(String questionInfo);

        void setPushUrl(String pushUrl);

        void setAnswerSuccess(boolean isSuccess);

        void setAnswerFinish();

        void setRetryQuestion();

        void setFaceFail(FaceRecognitionEntity faceRecognitionEntity);

        void setHasVideoFlowSuccess(boolean hasVideoFlowSuccess);
    }

    interface Model extends BaseModel {
        Observable<UserInfoEntity> getUserInfo(Map<String, Object> objectMap);

        Observable<QuestionEntity> updateVoiceQuestions(Map<String, Object> objectMap);

        Observable<String> getPushUrl(Map<String, Object> objectMap);

        Observable<String> sendRecordStreamRequest(Map<String, Object> objectMap);

        Observable<QuestionEntity> checkAnswer(Map<String, Object> objectMap);

        Observable<BaseFaceRecognitionEntity> detectLiveFace(Map<String, Object> objectMap);

        Observable<BaseEntity> saveVideo(Map<String, Object> objectMap);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getUserInfoData(String userCode, String productId);

        public abstract void updateVoiceQuestions(String unioncode);

        public abstract void checkAnswer(String unioncode, int questionId, String answerSeq);

        public abstract void detectLiveFace(String unioncode, String operatorName, String operatorIdCard);

        public abstract void getPushUrl(String unioncode);

        public abstract void sendRecordStreamRequest(String streamName, long questionStartTime, long questionEndTime, String answerSeq);

        public abstract void saveVideo(String unioncode, int recordingType, long investorEntryTime, String flowId);
    }
}
