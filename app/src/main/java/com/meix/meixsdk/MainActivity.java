package com.meix.meixsdk;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.meix.doublerecord.constant.KeyConstant;
import com.meix.doublerecord.ui.activity.WebViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_go_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(KeyConstant.URL_KEY, "http://dev.simu800.com/xqfunds/mobile/product/list?platform=1");
                WebViewActivity.startActivity(MainActivity.this, bundle);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case WebViewActivity.REQUEST_DOUBLE_VIDEO_CODE:
                    Toast.makeText(MainActivity.this, "双录成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
