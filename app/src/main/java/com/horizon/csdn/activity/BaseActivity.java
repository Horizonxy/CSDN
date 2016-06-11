package com.horizon.csdn.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.horizon.csdn.Application;
import com.horizon.csdn.R;
import com.horizon.csdn.util.SystemStatusManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BaseActivity extends AutoLayoutActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_left)
    ImageView btnLeft;
    @Bind(R.id.btn_right1)
    ImageView btnRight1;
    @Bind(R.id.btn_right2)
    ImageView btnRight2;

    FrameLayout layoutContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Application.getInstance().addAty(this);


//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);
    }

    public Resources getRes() {
        return Application.getInstance().getRes();
    }

    public ImageLoader getImageLoader() {
        return Application.getInstance().getImageLoader();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View layout = getLayoutInflater().inflate(R.layout.view_base, null);
        View vContent = getLayoutInflater().inflate(layoutResID, null);

        layoutContent = (FrameLayout) layout.findViewById(R.id.base_layout_content);
        layoutContent.addView(vContent);

        SystemStatusManager.setTranslucentStatus(this, R.color.main);

        super.setContentView(layout);

        ButterKnife.bind(this);
    }

    public void setTitle(String title) {
        if (title == null) {
            title = "";
        }
        tvTitle.setText(title);
    }

    @Override
    protected void onDestroy() {
        Application.getInstance().removeAty(this);
        super.onDestroy();
    }

    @OnClick(R.id.btn_left)
    public void leftClick() {
        finish();
    }

}
