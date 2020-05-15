package com.meix.doublerecord.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.meix.doublerecord.R;
import com.meix.doublerecord.base.BaseActivity;
import com.meix.doublerecord.base.GlideApp;
import com.meix.doublerecord.constant.Constants;
import com.meix.doublerecord.constant.GenerateTestUserSig;
import com.meix.doublerecord.constant.KeyConstant;
import com.meix.doublerecord.entity.FaceRecognitionEntity;
import com.meix.doublerecord.entity.QuestionEntity;
import com.meix.doublerecord.entity.UserInfoEntity;
import com.meix.doublerecord.entity.WebFromEntity;
import com.meix.doublerecord.mvp.contract.UserInfoContract;
import com.meix.doublerecord.mvp.model.UserInfoModel;
import com.meix.doublerecord.mvp.presenter.UserInfoPresenter;
import com.meix.doublerecord.widget.WaitRepayDialog;
import com.tencent.liteav.TXLiteAVCode;
import com.tencent.liteav.beauty.TXBeautyManager;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.tencent.trtc.TRTCCloudDef.TRTCRoleAnchor;
import static com.tencent.trtc.TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL;

/**
 * 双录主界面(用户信息)
 */
public class DoubleVideoMainActivity extends BaseActivity<UserInfoPresenter, UserInfoModel> implements UserInfoContract.View, View.OnClickListener {
    private final String TAG = DoubleVideoMainActivity.class.getSimpleName();
    private ImageView iv_back;
    private TextView mTvTip;
    private TextView mTvProductName;
    private TextView mTvUserName;
    private TextView mTvCardType;
    private TextView mTvCardNumber;
    private TextView mTvQuestionNumber;
    private Button mBtReturn;
    private Button mBtStartVideo;
    private LinearLayout mLlCurrentStatus;
    private LinearLayout mLlUserInfo;
    private RelativeLayout mRlVideo;
    private TXCloudVideoView mAnchorVideoView;
    private TXCloudVideoView trtc_tc_cloud_view;
    private LinearLayout mLlReplyQuestion;
    private TextView mTvCurrentQuestionNum;
    private TextView mTvQuestionContent;
    private LinearLayout llReplyFail;
    private TextView tvFailQuestion;
    private TextView tvCustomerTip;
    private LinearLayout ll_face_fail;
    private ImageView iv_user_photo;
    private LinearLayout mLlLiveConnectStatus;
    private ImageView mIvLiveConnectStatus;
    private TextView mTvLiveConnectStatus;
    private TextView mTvSecondCountDown;
    private LinearLayout mLlBottomFunctionArea;
    private View mLineHeight;
    private int currentPageStatus = 0;//0正常；1答题中 2 回答错误 3人脸识别失败
    private String mPushUrl = "";
    private TXLivePusher mLivePusher;
    private static final int REQ_PERMISSION_CODE = 0x1000;
    private static final int REQUEST_CODE = 101;
    private int mGrantedCount = 0;// 权限个数计数，获取Android系统权限
    private QuestionEntity mCurrentQutestionEntity;
    private String mUnionCode;
    private long questionStartTime;
    private long questionEndTime;
    private String answerSeq;
    private boolean hasFaceSuccess = true;
    private FaceRecognitionEntity mFaceRecognitionEntity;
    private UserInfoEntity mUserInfoEntity;
    private WebFromEntity mWebFromEntity;
    private MediaPlayer mMediaPlayer;
    private TRTCCloud mTRTCCloud;
    private List<String> mRemoteUidList;// 远端用户Id列表
    private List<TXCloudVideoView> mRemoteViewList;// 远端画面列表
    private long investorEntryTime;
    /**
     * 双录类型 1：人人 ，2：人机
     */
    private int recordingType = 1;
    private String mRoomId = "1256732";// 房间Id
    private String mUserId = "55713899";
    /**
     * 刷新人脸识别线程
     */
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mPresenter.detectLiveFace(mUnionCode, mUserInfoEntity.getOperatorName(), mUserInfoEntity.getOperatorIdCard());
            mHandler.postDelayed(this, 2000);
        }
    };

    public static void startActivity(Activity context, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, DoubleVideoMainActivity.class);
        intent.putExtra(KeyConstant.URL_BUNDLE_KEY, bundle);
        context.startActivityForResult(intent, REQUEST_CODE, bundle);
    }

    public static void startActivityWithOject(Activity context, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, DoubleVideoMainActivity.class);
        intent.putExtras(bundle);
        context.startActivityForResult(intent, REQUEST_CODE, bundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_double_video_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        mTvTip = (TextView) findViewById(R.id.tv_tip);
        mTvProductName = (TextView) findViewById(R.id.tv_product_name);
        mTvUserName = (TextView) findViewById(R.id.tv_user_name);
        mTvCardType = (TextView) findViewById(R.id.tv_card_type);
        mTvCardNumber = (TextView) findViewById(R.id.tv_card_number);
        mTvQuestionNumber = (TextView) findViewById(R.id.tv_question_number);
        mBtReturn = (Button) findViewById(R.id.bt_return);
        mBtStartVideo = (Button) findViewById(R.id.bt_start_video);
        mLlCurrentStatus = (LinearLayout) findViewById(R.id.ll_current_status);
        mLlUserInfo = (LinearLayout) findViewById(R.id.ll_user_info);
        mRlVideo = (RelativeLayout) findViewById(R.id.rl_video);
        mAnchorVideoView = (TXCloudVideoView) findViewById(R.id.anchor_video_view);
        mLlReplyQuestion = (LinearLayout) findViewById(R.id.ll_reply_question);
        mTvCurrentQuestionNum = (TextView) findViewById(R.id.tv_current_question_num);
        mTvQuestionContent = (TextView) findViewById(R.id.tv_question_content);
        mLineHeight = (View) findViewById(R.id.line_height);
        llReplyFail = (LinearLayout) findViewById(R.id.ll_reply_fail);
        tvFailQuestion = (TextView) findViewById(R.id.tv_fail_question);
        tvCustomerTip = (TextView) findViewById(R.id.tv_customer_tip);
        iv_user_photo = (ImageView) findViewById(R.id.iv_user_photo);
        ll_face_fail = (LinearLayout) findViewById(R.id.ll_face_fail);
        trtc_tc_cloud_view = (TXCloudVideoView) findViewById(R.id.trtc_tc_cloud_view);

        mLlLiveConnectStatus = (LinearLayout) findViewById(R.id.ll_live_connect_status);
        mIvLiveConnectStatus = (ImageView) findViewById(R.id.iv_live_connect_status);
        mTvLiveConnectStatus = (TextView) findViewById(R.id.tv_live_connect_status);
        mTvSecondCountDown = (TextView) findViewById(R.id.tv_second_count_down);
        mLlBottomFunctionArea = (LinearLayout) findViewById(R.id.ll_bottom_function_area);

    }

    @Override
    public void initListener() {
        mBtReturn.setOnClickListener(this);
        mBtStartVideo.setOnClickListener(this);
        iv_user_photo.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mLivePusher = new TXLivePusher(this);
        mRemoteUidList = new ArrayList<>();
        mRemoteViewList = new ArrayList<>();
        mRemoteViewList.add(trtc_tc_cloud_view);
        if (getIntent() != null) {
            mWebFromEntity = getIntent().getExtras().getParcelable(KeyConstant.URL_WEB_ENTITY_KEY);
            recordingType = mWebFromEntity.getRecordingType();
        }
        if (mWebFromEntity != null) {
            mPresenter.getUserInfoData(mWebFromEntity.getUserCode(), mWebFromEntity.getProductId());
        }
        checkPermission();
    }

    @Override
    public void setUserInfo(UserInfoEntity userInfo) {
        this.mUserInfoEntity = userInfo;
        mTvProductName.setText(getResources().getString(R.string.product_name) + (TextUtils.isEmpty(userInfo.getProductName()) ? "--" : userInfo.getProductName()));
        mTvUserName.setText(getResources().getString(R.string.user_name) + userInfo.getOperatorName());
        mTvCardType.setText(getResources().getString(R.string.card_type) + userInfo.getOperatorIdCardType());
        mTvCardNumber.setText(getResources().getString(R.string.card_number) + userInfo.getOperatorIdCard());
        mUnionCode = userInfo.getUnioncode();
        mPresenter.updateVoiceQuestions(userInfo.getUnioncode());
        mPresenter.getPushUrl(userInfo.getUnioncode());
        if (recordingType == 1) {
            currentPageStatus = 4;
            startLoadingAnim();
        } else {
            currentPageStatus = 0;
        }
        setPageStatus();
    }

    /**
     * 等待直播接入得加载
     */
    private void startLoadingAnim() {
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.loading_anim);
        LinearInterpolator lir = new LinearInterpolator();
        rotate.setInterpolator(lir);
        mIvLiveConnectStatus.startAnimation(rotate);
    }

    @Override
    public void setQuestionInfoStr(String questionInfo) {
        mTvQuestionNumber.setText(questionInfo);
    }

    @Override
    public void setQuestionInfo(QuestionEntity questionInfo) {
        mCurrentQutestionEntity = questionInfo;
        mTvCurrentQuestionNum.setText("问题" + (questionInfo.getCurrent() + 1) + ":");
        mTvQuestionContent.setText(questionInfo.getQuestionContet() + "");
    }

    /**
     * 播放题目音频
     */
    private void playAudio() {
        mMediaPlayer = MediaPlayer.create(this, Uri.parse(mCurrentQutestionEntity.getFileUrl()));
        questionStartTime = System.currentTimeMillis();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                questionEndTime = System.currentTimeMillis();
                answerSeq = String.valueOf(UUID.randomUUID());
                mPresenter.sendRecordStreamRequest(mUnionCode, questionStartTime, questionEndTime, answerSeq);
                showDialog();
                playReplyAudio();
            }
        });
        mMediaPlayer.start();
    }

    /**
     * 播放请回答语音地址
     */
    private void playReplyAudio() {
        mMediaPlayer = MediaPlayer.create(this, Uri.parse(Constants.pleaseAnswerUrl));
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                waitRepayDialog.setQuestionTime(mCurrentQutestionEntity.getAnswerTime());
                waitRepayDialog.setStatus(1);
            }
        });
        mMediaPlayer.start();
    }

    private WaitRepayDialog waitRepayDialog;

    private void showDialog() {
        waitRepayDialog = new WaitRepayDialog(mContext);
        waitRepayDialog.setStartCheckAnswerListener(new WaitRepayDialog.StartCheckAnswerListener() {
            @Override
            public void onStart() {
                mPresenter.checkAnswer(mUnionCode, mCurrentQutestionEntity.getQuestionId(), answerSeq);
            }
        });
        waitRepayDialog.show();
    }

    private void closeDialog() {
        if (waitRepayDialog != null) {
            waitRepayDialog.dismiss();
        }
    }

    @Override
    public void setPushUrl(String pushUrl) {
        mPushUrl = pushUrl;
    }

    @Override
    public void setAnswerSuccess(boolean isSuccess) {
        retryCount = 0;
        mRetryHandler.removeCallbacksAndMessages(null);
        if (isSuccess) {
            playAudio();
        } else {
            currentPageStatus = 2;
            setPageStatus();
            mLivePusher.stopPusher();
            mLivePusher.stopCameraPreview(true);
        }
        closeDialog();
    }


    @Override
    public void setAnswerFinish() {
        mPresenter.saveVideo(mUnionCode, recordingType, investorEntryTime, mWebFromEntity.getFlowId());
    }

    @Override
    public void setRetryQuestion() {
        closeDialog();
        playAudio();
    }

    @Override
    public void setFaceFail(FaceRecognitionEntity info) {
        mHandler.removeCallbacksAndMessages(null);
        this.mFaceRecognitionEntity = info;
        hasFaceSuccess = false;
        currentPageStatus = 3;
        if (waitRepayDialog != null) {
            waitRepayDialog.dismiss();
        }
        stopAudio();
        setPageStatus();
    }

    /**
     * 停止音频
     */
    private void stopAudio() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
        }
    }

    @Override
    public void setHasVideoFlowSuccess(boolean hasVideoFlowSuccess) {
        if (hasVideoFlowSuccess) {
            Toast.makeText(this, "双录成功", Toast.LENGTH_SHORT).show();
            backWebView();
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBtReturn) {
            finish();
        }
        if (v == mBtStartVideo) {
            if (checkPermission()) {
                retryCount = 0;
                if (currentPageStatus == 2) {
                    currentPageStatus = 0;
                    setPageStatus();
                    mLivePusher.stopPusher();
                } else {
                    startPush();
                    playAudio();
                }
            }
        }
        if (v == iv_user_photo) {
            List<String> imgUrls = new ArrayList<>();
            imgUrls.add(mFaceRecognitionEntity.getPic_full_url());
            ShowBigImageActivity.startImagePagerActivity(this, imgUrls, 0);
        }
        if (v == iv_back) {
            finish();
        }
    }

    /**
     * 审核成功回到web页并刷新
     */
    private void backWebView() {
        Intent intent = new Intent();
        intent.putExtra(KeyConstant.URL_VIDEO_SUCCESS_KEY, "video_success");
        setResult(RESULT_OK, intent);
    }

    private void startPush() {
        investorEntryTime = System.currentTimeMillis();
        currentPageStatus = 1;
        setPageStatus();
        mLivePusher.startCameraPreview(mAnchorVideoView);
        mLivePusher.startPusher(mPushUrl.trim());
        mHandler.postDelayed(mRunnable, 3000);
    }

    /**
     * 进入直播间
     */
    private void enterRoom() {
        setPageStatus();
        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());
        mTRTCCloud.setListener(new TRTCCloudImplListener(DoubleVideoMainActivity.this));

        // 初始化配置 SDK 参数
        TRTCCloudDef.TRTCParams trtcParams = new TRTCCloudDef.TRTCParams();
        trtcParams.sdkAppId = GenerateTestUserSig.SDKAPPID;
        trtcParams.userId = mUserId;
        trtcParams.roomId = Integer.parseInt(mRoomId);
        // userSig是进入房间的用户签名，相当于密码（这里生成的是测试签名，正确做法需要业务服务器来生成，然后下发给客户端）
        trtcParams.userSig = GenerateTestUserSig.genTestUserSig(trtcParams.userId);
        trtcParams.role = TRTCRoleAnchor;
        // 进入通话
        mTRTCCloud.enterRoom(trtcParams, TRTC_APP_SCENE_VIDEOCALL);
        // 开启本地声音采集并上行
        mTRTCCloud.startLocalAudio();
        // 开启本地画面采集并上行
        mTRTCCloud.startLocalPreview(true, mAnchorVideoView);
        /**
         * 设置默认美颜效果（美颜效果：自然，美颜级别：5, 美白级别：1）
         * 美颜风格.三种美颜风格：0 ：光滑  1：自然  2：朦胧
         * 视频通话场景推荐使用“自然”美颜效果
         */
        TXBeautyManager beautyManager = mTRTCCloud.getBeautyManager();
        beautyManager.setBeautyStyle(Constants.BEAUTY_STYLE_NATURE);
        beautyManager.setBeautyLevel(5);
        beautyManager.setWhitenessLevel(1);

        TRTCCloudDef.TRTCVideoEncParam encParam = new TRTCCloudDef.TRTCVideoEncParam();
        encParam.videoResolution = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_640_360;
        encParam.videoFps = Constants.VIDEO_FPS;
        encParam.videoBitrate = Constants.RTC_VIDEO_BITRATE;
        encParam.videoResolutionMode = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_MODE_PORTRAIT;
        mTRTCCloud.setVideoEncoderParam(encParam);
    }

    private class TRTCCloudImplListener extends TRTCCloudListener {

        private WeakReference<DoubleVideoMainActivity> mContext;

        public TRTCCloudImplListener(DoubleVideoMainActivity activity) {
            super();
            mContext = new WeakReference<>(activity);
        }

        @Override
        public void onUserVideoAvailable(String userId, boolean available) {
            Log.d(TAG, "onUserVideoAvailable userId " + userId + ",available " + available);

            TRTCCloudDef.TRTCTranscodingConfig config = new TRTCCloudDef.TRTCTranscodingConfig();
            config.videoWidth = 720;
            config.videoHeight = 1280;
            config.videoBitrate = 1500;
            config.videoFramerate = 20;
            config.videoGOP = 2;
            config.audioSampleRate = 48000;
            config.audioBitrate = 64;
            config.audioChannels = 2;
            // 采用预排版模式
            config.mode = TRTCCloudDef.TRTC_TranscodingConfigMode_Template_PresetLayout;
            config.mixUsers = new ArrayList<>();
            TRTCCloudDef.TRTCMixUser local = new TRTCCloudDef.TRTCMixUser();
            local.userId = mUserId;
            local.zOrder = 0;   // zOrder 为0代表主播画面位于最底层
            local.roomId = mRoomId;
            local.width = 720;
            local.height = 1280;
            local.x = 0;
            local.y = 0;
            config.mixUsers.add(local);

            TRTCCloudDef.TRTCMixUser remote1 = new TRTCCloudDef.TRTCMixUser();
            remote1.userId = userId;
            remote1.zOrder = 1;
            local.width = 150;
            local.height = 200;
            local.x = 0;
            local.y = 0;
            config.mixUsers.add(remote1);
            mTRTCCloud.setMixTranscodingConfig(config);
            int index = mRemoteUidList.indexOf(userId);
            if (available) {
                if (index != -1) { //如果mRemoteUidList有，就不重复添加
                    return;
                }
                mRemoteUidList.add(userId);
                refreshRemoteVideoViews();
            } else {
                if (index == -1) { //如果mRemoteUidList没有，说明已关闭画面
                    return;
                }
                /// 关闭用户userId的视频画面
                mTRTCCloud.stopRemoteView(userId);
                mRemoteUidList.remove(index);
                refreshRemoteVideoViews();
            }

        }

        @Override
        public void onUserAudioAvailable(String s, boolean b) {
            super.onUserAudioAvailable(s, b);
            TRTCCloudDef.TRTCTranscodingConfig config = new TRTCCloudDef.TRTCTranscodingConfig();
            config.videoWidth = 720;
            config.videoHeight = 1280;
            config.videoBitrate = 1500;
            config.videoFramerate = 20;
            config.videoGOP = 2;
            config.audioSampleRate = 48000;
            config.audioBitrate = 64;
            config.audioChannels = 2;
            // 采用预排版模式
            config.mode = TRTCCloudDef.TRTC_TranscodingConfigMode_Template_PresetLayout;
            config.mixUsers = new ArrayList<>();

            TRTCCloudDef.TRTCMixUser local = new TRTCCloudDef.TRTCMixUser();
            local.userId = "12122";
            local.zOrder = 0;   // zOrder 为0代表主播画面位于最底层
            local.roomId = mRoomId;
            local.width = 720;
            local.height = 1280;
            config.mixUsers.add(local);

            TRTCCloudDef.TRTCMixUser remote1 = new TRTCCloudDef.TRTCMixUser();
            remote1.userId = mUserId;
            remote1.zOrder = 1;
            local.width = 150;
            local.height = 200;
            config.mixUsers.add(remote1);
            mTRTCCloud.setMixTranscodingConfig(config);
        }

        private void refreshRemoteVideoViews() {
            for (int i = 0; i < mRemoteViewList.size(); i++) {
                if (i < mRemoteUidList.size()) {
                    String remoteUid = mRemoteUidList.get(i);
                    mRemoteViewList.get(i).setVisibility(View.VISIBLE);
                    // 开始显示用户userId的视频画面
                    mTRTCCloud.startRemoteView(remoteUid, mRemoteViewList.get(i));
                } else {
                    mRemoteViewList.get(i).setVisibility(View.GONE);
                }
            }
        }

        // 错误通知监听，错误通知意味着 SDK 不能继续运行
        @Override
        public void onError(int errCode, String errMsg, Bundle extraInfo) {
            Log.d(TAG, "sdk callback onError");
            DoubleVideoMainActivity activity = mContext.get();
            if (activity != null) {
                Toast.makeText(activity, "onError: " + errMsg + "[" + errCode + "]", Toast.LENGTH_SHORT).show();
                if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
                    activity.exitRoom();
                }
            }
        }
    }

    private void setPageStatus() {
        switch (currentPageStatus) {
            case 0://人机初始界面
                mLlUserInfo.setVisibility(View.VISIBLE);
                mLlCurrentStatus.setVisibility(View.VISIBLE);
                mBtReturn.setVisibility(View.VISIBLE);
                mLineHeight.setVisibility(View.GONE);
                mLlReplyQuestion.setVisibility(View.GONE);
                mRlVideo.setVisibility(View.GONE);
                llReplyFail.setVisibility(View.GONE);
                tvCustomerTip.setVisibility(View.GONE);
                mTvTip.setVisibility(View.VISIBLE);
                mBtStartVideo.setVisibility(View.VISIBLE);
                break;
            case 1://人机答题中
                retryCount = 0;
                mLineHeight.setVisibility(View.VISIBLE);
                mLlReplyQuestion.setVisibility(View.VISIBLE);
                mTvQuestionNumber.setVisibility(View.VISIBLE);
                mTvTip.setVisibility(View.VISIBLE);
                mRlVideo.setVisibility(View.VISIBLE);
                mLlUserInfo.setVisibility(View.GONE);
                mBtReturn.setVisibility(View.GONE);
                mLlCurrentStatus.setVisibility(View.GONE);
                mBtReturn.setVisibility(View.GONE);
                llReplyFail.setVisibility(View.GONE);
                tvCustomerTip.setVisibility(View.GONE);
                ll_face_fail.setVisibility(View.GONE);
                mBtStartVideo.setText("重新双录");
                break;
            case 2://答题错误
                mLlUserInfo.setVisibility(View.GONE);
                mLlCurrentStatus.setVisibility(View.GONE);
                mLineHeight.setVisibility(View.GONE);
                mLlReplyQuestion.setVisibility(View.GONE);
                mRlVideo.setVisibility(View.GONE);
                mTvQuestionNumber.setVisibility(View.GONE);
                ll_face_fail.setVisibility(View.GONE);
                mLivePusher.stopPusher();
                mLivePusher.stopCameraPreview(true);
                mBtReturn.setVisibility(View.VISIBLE);
                llReplyFail.setVisibility(View.VISIBLE);
                tvCustomerTip.setVisibility(View.VISIBLE);
                String content = "您的【问题" + (mCurrentQutestionEntity.getCurrent() + 1) + "：" + mCurrentQutestionEntity.getQuestionContet() + "】的回复不符合合规要求，请确认相关信息！";
                SpannableStringBuilder spannable = new SpannableStringBuilder(content);
                spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#188FFF")), 2, content.length() - 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvFailQuestion.setText(spannable);
                mTvTip.setVisibility(View.GONE);
                mBtStartVideo.setText("重新双录");
                break;
            case 3://人脸识别失败
                retryCount = 0;
                mLineHeight.setVisibility(View.VISIBLE);
                mLlReplyQuestion.setVisibility(View.VISIBLE);
                mTvQuestionNumber.setVisibility(View.VISIBLE);
                mTvTip.setVisibility(View.VISIBLE);
                mRlVideo.setVisibility(View.GONE);
                mLlUserInfo.setVisibility(View.GONE);
                mBtReturn.setVisibility(View.GONE);
                mLlCurrentStatus.setVisibility(View.GONE);
                mBtReturn.setVisibility(View.GONE);
                llReplyFail.setVisibility(View.GONE);
                tvCustomerTip.setVisibility(View.VISIBLE);
                ll_face_fail.setVisibility(View.VISIBLE);
                GlideApp.with(mContext)
                        .load(mFaceRecognitionEntity.getPic_full_url())
                        .into(iv_user_photo);
                mBtStartVideo.setText("重新双录");
                mLivePusher.stopPusher();
                mLivePusher.stopCameraPreview(true);
                break;
            case 4://人人直播等待
                mLlUserInfo.setVisibility(View.VISIBLE);
                mLlCurrentStatus.setVisibility(View.GONE);
                mBtReturn.setVisibility(View.VISIBLE);
                mLineHeight.setVisibility(View.GONE);
                mLlReplyQuestion.setVisibility(View.GONE);
                mRlVideo.setVisibility(View.GONE);
                llReplyFail.setVisibility(View.GONE);
                tvCustomerTip.setVisibility(View.GONE);
                mTvTip.setVisibility(View.GONE);
                mLlLiveConnectStatus.setVisibility(View.VISIBLE);
                mLlBottomFunctionArea.setVisibility(View.GONE);
                break;
            case 5://接入成功
                mIvLiveConnectStatus.setImageResource(R.mipmap.bg_connect_success);
                mTvLiveConnectStatus.setText(getResources().getString(R.string.connect_success_str));
                mTvSecondCountDown.setText(waitTime + "s");
                mCountDownHandler.postDelayed(mCountDownRunnable, 1000);
                break;
            case 6://开始直播
                mLineHeight.setVisibility(View.VISIBLE);
                mLlReplyQuestion.setVisibility(View.VISIBLE);
                mTvQuestionNumber.setVisibility(View.VISIBLE);
                mTvTip.setVisibility(View.VISIBLE);
                mTvTip.setText(getResources().getString(R.string.please_handle_idcard_str));
                mRlVideo.setVisibility(View.VISIBLE);
                mLlUserInfo.setVisibility(View.GONE);
                mLlCurrentStatus.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private int waitTime = 5;
    private Handler mCountDownHandler = new Handler();
    private Runnable mCountDownRunnable = new Runnable() {
        @Override
        public void run() {
            waitTime--;
            mTvSecondCountDown.setText(waitTime + "s");
            if (waitTime == 0) {
                //倒计时结束开始打开摄像头
                currentPageStatus = 6;
                setPageStatus();
                enterRoom();
                mCountDownHandler.removeCallbacks(this);
            } else {
                mCountDownHandler.postDelayed(this, 1000);
            }
        }
    };

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(DoubleVideoMainActivity.this,
                        permissions.toArray(new String[0]),
                        REQ_PERMISSION_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSION_CODE) {
            for (int ret : grantResults) {
                if (PackageManager.PERMISSION_GRANTED == ret) mGrantedCount++;
            }
            if (mGrantedCount == permissions.length) {
                if (mCurrentQutestionEntity != null) {
                    if (recordingType == 2) {
                        startPush(); //首次启动，权限都获取到，才能正常进入通话
                        playAudio();
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.rtc_permisson_error_tip), Toast.LENGTH_SHORT).show();
            }
            mGrantedCount = 0;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLivePusher != null) {
            mLivePusher.stopPusher();
            mLivePusher.stopCameraPreview(true);
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mRetryHandler != null) {
            mRetryHandler.removeCallbacksAndMessages(null);
        }
        stopAudio();
        exitRoom();
    }

    /**
     * 离开通话
     */
    private void exitRoom() {
        if (mTRTCCloud != null) {
            mTRTCCloud.stopLocalAudio();
            mTRTCCloud.stopLocalPreview();
            mTRTCCloud.exitRoom();
            //销毁 trtc 实例
            mTRTCCloud.setListener(null);
        }
        mTRTCCloud = null;
        TRTCCloud.destroySharedInstance();
    }

    private int retryCount = 0;
    /**
     * 检测答案是否正确线程
     */
    private Handler mRetryHandler = new Handler();
    private Runnable mRetryRunnable = new Runnable() {
        @Override
        public void run() {
            retryCount++;
            mPresenter.checkAnswer(mUnionCode, mCurrentQutestionEntity.getQuestionId(), answerSeq);
        }
    };

    @Override
    public void onError(String msg) {
        super.onError(msg);
        if (msg.contains("checkAnswer")) {
            if (retryCount <= 4 && hasFaceSuccess) {
                mRetryHandler.postDelayed(mRetryRunnable, 1500);
            } else {
                mHandler.removeCallbacksAndMessages(null);
                Toast.makeText(mContext, "失败", Toast.LENGTH_LONG).show();
                closeDialog();
                currentPageStatus = 2;
                setPageStatus();
                mRetryHandler.removeCallbacksAndMessages(null);
            }
        }
    }
}
