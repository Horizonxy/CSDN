package com.horizon.csdn.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.horizon.csdn.Application;
import com.horizon.csdn.Constants;
import com.horizon.csdn.R;
import com.horizon.csdn.activity.PictureDetailActivity;
import com.horizon.csdn.adapter.quickadapter.BaseAdapterHelper;
import com.horizon.csdn.adapter.quickadapter.MultiItemTypeSupport;
import com.horizon.csdn.adapter.quickadapter.QuickAdapter;
import com.horizon.csdn.util.SmallPicInfo;
import com.zhy.bean.News;

import java.util.ArrayList;


public class NewsDetailAdapter extends QuickAdapter<News> {

    private boolean mHasTitle;
    private OnTitleReceive onTitleReceive;

    public NewsDetailAdapter(Context context, ArrayList<News> data, MultiItemTypeSupport<News> multiItemSupport, OnTitleReceive onTitleReceive) {
        super(context, data, multiItemSupport);
        this.onTitleReceive = onTitleReceive;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, final News item) {

        switch (item.getType()) {
            case News.NewsType.TITLE:
                if (!mHasTitle) {
                    mHasTitle = true;
                    onTitleReceive.onTitle(item.getTitle());
                }
                break;
            case News.NewsType.SUMMARY:
                helper.setText(R.id.text, item.getSummary());
                break;
            case News.NewsType.CONTENT:
                helper.setText(R.id.text, "\u3000\u3000" + Html.fromHtml(item.getContent()));
                break;
            case News.NewsType.IMG:
                helper.setImageBuilder(R.id.imageView, item.getImageLink(), Application.getInstance().getDefaultOptions())
                .setOnClickListener(R.id.imageView, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView imageView = (ImageView) v;
                        imageView.setDrawingCacheEnabled(true);
                        Bitmap bitmap = imageView.getDrawingCache();

                        int[] screenLocation = new int[2];
                        imageView.getLocationOnScreen(screenLocation);

                        SmallPicInfo info = new SmallPicInfo(item.getImageLink(), screenLocation[0], screenLocation[1], imageView.getWidth(),imageView.getHeight(), 0, Bitmap.createBitmap(bitmap));

                        Intent intent = new Intent(context, PictureDetailActivity.class);
                        intent.putExtra(Constants.BUNDLE_PIC_INFOS, info);
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(0, 0);

                        imageView.setDrawingCacheEnabled(false);
                    }
                });
                break;
            case News.NewsType.BOLD_TITLE:
                helper.setText(R.id.text, "\u3000\u3000" + Html.fromHtml(item.getContent()));
                break;
        }

    }

    public interface OnTitleReceive {
        public void onTitle(String title);
    }
}
