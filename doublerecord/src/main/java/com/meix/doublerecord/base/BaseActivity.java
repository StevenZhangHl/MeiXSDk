package com.meix.doublerecord.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.meix.doublerecord.R;
import com.meix.doublerecord.rx.RxManager;
import com.meix.doublerecord.util.TUtil;
import com.meix.doublerecord.util.NetWorkUtils;

public abstract class BaseActivity<T extends BasePresenter, E extends BaseModel> extends AppCompatActivity implements BaseView {
    public T mPresenter;
    public E mModel;
    public Context mContext;
    public RxManager mRxManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setExitTransition(new Slide(Gravity.LEFT));
        getWindow().setEnterTransition(new Slide(Gravity.RIGHT));
        super.onCreate(savedInstanceState);
        mRxManager = new RxManager();
        doBeforeSetContentView();
        setContentView(getLayoutId());
        mContext = this;
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (mPresenter != null) {
            mPresenter.mContext = this;
        }
        // 是否检查网络连接
        if (isCheckNetWork()) {
            isNetConnected();
        }
        this.initPresenter();
        this.initView(savedInstanceState);
        this.initListener();
        this.initData();
    }

    /**
     * 设置layout前配置
     */
    private void doBeforeSetContentView() {
        // 无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setStatusBar();
    }

    /**
     * 获取布局文件
     *
     * @return 布局Id
     */
    @LayoutRes
    public abstract int getLayoutId();

    /**
     * 简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
     */
    public abstract void initPresenter();

    /**
     * 初始化view
     *
     * @param savedInstanceState 保存数据
     */
    public abstract void initView(Bundle savedInstanceState);

    /**
     * 初始化监听事件
     */
    public abstract void initListener();
    /**
     * 初始化数据
     *
     */
    public abstract void initData();

    /**
     * 是否使用自定义LoadingLayout，适用于头部不是常规Toolbar的页面
     *
     * @return boolean
     */
    protected boolean isCustomLoadingLayout() {
        return false;
    }


    /**
     * 白色状态栏
     * 6.0以上状态栏修改为白色，状态栏字体为黑色
     * 6.0以下状态栏为黑色
     */
    protected void setWhiteStatusBar(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setColor(this, getResources().getColor(color), 0);
        } else {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.black), 0);
        }
    }

    /**
     * 沉浸式状态栏
     */
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.mainColor), 0);
    }

    /**
     * 是否检查网络设置，默认不检查
     */
    protected boolean isCheckNetWork() {
        return false;
    }

    /**
     * 网络链接状态
     */
    protected void isNetConnected() {
        //网络
        if (!NetWorkUtils.isNetConnected(this)) {
//            showNoNetwork();
        }
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onError(String msg) {
    }
}
