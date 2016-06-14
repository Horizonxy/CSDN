package com.horizon.csdn.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.horizon.csdn.Application;
import com.horizon.csdn.Constants;
import com.horizon.csdn.R;
import com.horizon.csdn.activity.NewsDetailActivity;
import com.horizon.csdn.adapter.quickadapter.BaseAdapterHelper;
import com.horizon.csdn.adapter.quickadapter.MultiItemTypeSupport;
import com.horizon.csdn.adapter.quickadapter.QuickAdapter;
import com.zhy.bean.NewsItem;

import java.util.ArrayList;

public class CsdnListAdapter extends QuickAdapter<NewsItem> {

    private Context context;

    public CsdnListAdapter(Context context, ArrayList<NewsItem> data, MultiItemTypeSupport<NewsItem> multiItemSupport) {
        super(context, data, multiItemSupport);
        this.context = context;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, final NewsItem item) {
        if(item == null){
            return;
        }
        helper.setText(R.id.tv_title, item.getTitle() == null ? "" : item.getTitle())
                .setText(R.id.tv_content, item.getContent() == null ? "" : item.getContent())
                .setText(R.id.tv_time, item.getDate() == null ? "" : item.getDate())
                .setOnClickListener(R.id.item_layout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, NewsDetailActivity.class);
                        intent.putExtra(Constants.BUNDLE_NEWS_URL, item.getLink());
                        context.startActivity(intent);
                    }
                });
        switch (helper.layoutId){
            case R.layout.item_csdn_news_list:

                helper.setImageBuilder(R.id.iv_banner, item.getImgLink(), Application.getInstance().getDefaultOptions());
                break;
            case R.layout.item_csdn_news_list_no_banner:

                break;
        }
    }
}
