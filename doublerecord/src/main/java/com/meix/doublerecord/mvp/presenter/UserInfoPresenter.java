package com.meix.doublerecord.mvp.presenter;

import android.util.Log;
import android.widget.Toast;

import com.meix.doublerecord.api.ApiRequest;
import com.meix.doublerecord.entity.BaseEntity;
import com.meix.doublerecord.entity.BaseFaceRecognitionEntity;
import com.meix.doublerecord.entity.QuestionEntity;
import com.meix.doublerecord.entity.UserInfoEntity;
import com.meix.doublerecord.mvp.contract.UserInfoContract;
import com.meix.doublerecord.rx.BaseObserver;

public class UserInfoPresenter extends UserInfoContract.Presenter {
    private boolean hasFaceSuccess = true;

    @Override
    public void getUserInfoData(String userCode, String productId) {
        mRxManager.add(mModel.getUserInfo(ApiRequest.getUserInfo(userCode, productId)).subscribeWith(new BaseObserver<UserInfoEntity>(mContext, true) {

            @Override
            protected void onSuccess(UserInfoEntity userInfoEntity) {
                mView.setUserInfo(userInfoEntity);
            }

            @Override
            protected void onError(String message) {
                mView.onError(message);
            }
        }));
    }

    @Override
    public void updateVoiceQuestions(String unioncode) {
        mRxManager.add(mModel.updateVoiceQuestions(ApiRequest.updateVoiceQuestions(unioncode)).subscribeWith(new BaseObserver<QuestionEntity>(mContext, false) {

            @Override
            protected void onSuccess(QuestionEntity questionEntity) {
                mView.setQuestionInfoStr("状态：" + "第" + (questionEntity.getCurrent() + 1) + "题，共" + questionEntity.getTotal() + "题");
                mView.setQuestionInfo(questionEntity);
            }

            @Override
            protected void onError(String message) {
                mView.onError(message);
            }
        }));
    }

    @Override
    public void checkAnswer(String unioncode, final int questionId, String answerSeq) {
        mRxManager.add(mModel.checkAnswer(ApiRequest.checkAnswer(unioncode, questionId, answerSeq)).subscribeWith(new BaseObserver<QuestionEntity>(mContext, false) {

            @Override
            protected void onSuccess(QuestionEntity questionEntity) {
                Log.i("hasFaceSuccess", hasFaceSuccess + "");
                if (hasFaceSuccess) {
                    if (questionEntity.getStatus().equals("wrong")) {
                        mView.setAnswerSuccess(false);
                    } else if (questionEntity.getStatus().equals("retry")) {
                        mView.setRetryQuestion();
                    } else {
                        if (questionEntity.getCurrent() + 1 == questionEntity.getTotal()) {
                            mView.setAnswerFinish();
                        } else {
                            mView.setQuestionInfoStr("状态：" + "第" + (questionEntity.getCurrent() + 1) + "题，共" + questionEntity.getTotal() + "题");
                            mView.setQuestionInfo(questionEntity);
                            mView.setAnswerSuccess(true);
                        }
                    }
                }
            }

            @Override
            protected void onError(String message) {
                if (hasFaceSuccess) {
                    mView.onError("checkAnswer" + message);
                }
            }
        }));
    }

    @Override
    public void detectLiveFace(String unioncode, String operatorName, String operatorIdCard) {
        mRxManager.add(mModel.detectLiveFace(ApiRequest.detectLiveFace(unioncode, operatorName, operatorIdCard)).subscribeWith(new BaseObserver<BaseFaceRecognitionEntity>(mContext, false) {

            @Override
            protected void onSuccess(BaseFaceRecognitionEntity baseFaceRecognitionEntity) {
                Toast.makeText(mContext, "detectLiveFace:success" + baseFaceRecognitionEntity.isData(), Toast.LENGTH_SHORT).show();
                if (!baseFaceRecognitionEntity.isData()) {
                    hasFaceSuccess = false;
                    mView.setFaceFail(baseFaceRecognitionEntity.getJsonObject());
                } else {
                    hasFaceSuccess = true;
                }
            }

            @Override
            protected void onError(String message) {
                Toast.makeText(mContext, "detectLiveFace" + message, Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public void getPushUrl(String unioncode) {
        mRxManager.add(mModel.getPushUrl(ApiRequest.getPushUrl(unioncode)).subscribeWith(new BaseObserver<String>(mContext, false) {

            @Override
            protected void onSuccess(String s) {
                mView.setPushUrl(s);
            }

            @Override
            protected void onError(String message) {

            }
        }));
    }

    @Override
    public void sendRecordStreamRequest(String streamName, long questionStartTime, long questionEndTime, String answerSeq) {
        mRxManager.add(mModel.sendRecordStreamRequest(ApiRequest.sendRecordStreamRequest(streamName, questionStartTime, questionEndTime, answerSeq)).subscribeWith(new BaseObserver<String>(mContext, false) {

            @Override
            protected void onSuccess(String s) {
            }

            @Override
            protected void onError(String message) {
                mView.onError("sendRecordStreamRequest" + message);
            }
        }));
    }

    @Override
    public void saveVideo(String unioncode, int recordingType, long investorEntryTime, String flowId) {
        mRxManager.add(mModel.saveVideo(ApiRequest.saveVideo(unioncode, recordingType, investorEntryTime, flowId)).subscribeWith(new BaseObserver<BaseEntity>() {

            @Override
            protected void onSuccess(BaseEntity baseEntity) {
                mView.setHasVideoFlowSuccess(true);
            }

            @Override
            protected void onError(String message) {

            }
        }));
    }
}
