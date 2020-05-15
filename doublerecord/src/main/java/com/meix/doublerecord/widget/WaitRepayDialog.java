package com.meix.doublerecord.widget;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.flyco.dialog.widget.base.BaseDialog;
import com.meix.doublerecord.R;


public class WaitRepayDialog extends BaseDialog<WaitRepayDialog> {
    private Context mContext;
    private ImageView mIvStatus;
    private TextView mTvSecond;
    private TextView mTvTotalSecond;
    private TextView mTvYes;
    private TextView mTvSecondTitle;
    private TextView mTvNo;
    private Handler mHandler = new Handler();
    private int questionTime;

    public interface StartCheckAnswerListener {
        void onStart();
    }

    public StartCheckAnswerListener startCheckAnswerListener;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            questionTime--;
            mTvSecond.setText(questionTime + "s");
            if (questionTime == 0) {
                mHandler.removeCallbacks(this);
                startCheckAnswerListener.onStart();
                setStatus(2);
            } else {
                mHandler.postDelayed(this, 1000);
            }
        }
    };

    public WaitRepayDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView() {
        View root = getLayoutInflater().inflate(R.layout.dialog_wait_reply_view, null);
        mIvStatus = (ImageView) root.findViewById(R.id.iv_status);
        mTvSecond = (TextView) root.findViewById(R.id.tv_second);
        mTvTotalSecond = (TextView) root.findViewById(R.id.tv_total_second);
        mTvYes = (TextView) root.findViewById(R.id.tv_yes);
        mTvSecondTitle = (TextView) root.findViewById(R.id.tv_second_title);
        mTvNo = (TextView) root.findViewById(R.id.tv_no);
        return root;
    }

    @Override
    public void setUiBeforShow() {
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        widthScale(0.85f); // 屏幕宽度
    }

    /**
     * 设置当前答题状态
     *
     * @param status 0：加载回答要求中 1：答题中 2：检查中
     */
    public void setStatus(int status) {
        switch (status) {
            case 0:
                break;
            case 1:
                mIvStatus.setImageResource(R.mipmap.icon_wait_reply);
                mTvSecond.setVisibility(View.VISIBLE);
                mTvSecond.setText(questionTime + "s");
                mTvSecondTitle.setText("  或  ");
                mHandler.postDelayed(mRunnable, 1000);
                mTvTotalSecond.setText("请在" + questionTime + "秒内回答");
                mTvYes.setVisibility(View.VISIBLE);
                mTvNo.setVisibility(View.VISIBLE);
                break;
            case 2:
                mIvStatus.setImageResource(R.mipmap.icon_check_question);
                mTvTotalSecond.setText(mContext.getResources().getString(R.string.checking_question));
                mTvSecondTitle.setText("请稍后...");
                mTvYes.setVisibility(View.GONE);
                mTvNo.setVisibility(View.GONE);
                mTvSecond.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    public void setQuestionTime(int time) {
        this.questionTime = time;
    }

    public void setStartCheckAnswerListener(StartCheckAnswerListener startCheckAnswerListener) {
        this.startCheckAnswerListener = startCheckAnswerListener;
    }
}
