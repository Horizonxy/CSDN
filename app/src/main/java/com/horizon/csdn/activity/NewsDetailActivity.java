package com.horizon.csdn.activity;

import android.os.AsyncTask;
import android.os.Bundle;

import com.horizon.csdn.Application;
import com.horizon.csdn.Constants;
import com.horizon.csdn.R;
import com.horizon.csdn.adapter.NewsDetailAdapter;
import com.horizon.csdn.adapter.quickadapter.MultiItemTypeSupport;
import com.horizon.csdn.db.CommonDaoImpl;
import com.horizon.csdn.util.GsonUtils;
import com.horizon.csdn.util.NetUtils;
import com.horizon.csdn.vo.CommonCacheVo;
import com.horizon.csdn.widget.InitializeListView;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.zhy.bean.CommonException;
import com.zhy.bean.News;
import com.zhy.biz.NewsItemBiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class NewsDetailActivity extends BaseActivity {

    @Bind(R.id.news_detail_list)
    InitializeListView mListView;

    private ArrayList<News> mData;
    private NewsItemBiz mNewsItemBiz;
    private NewsDetailAdapter mAdapter;

    private Map<String, Object> mCahceMap = new HashMap<String, Object>();
    private static final String DATA_TYPE = "csdn_news_detail";

    private CommonDaoImpl mCommonDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        setBtnLeft(MaterialDesignIconic.Icon.gmi_mail_reply_all);

        mNewsItemBiz = new NewsItemBiz();
        String url = getIntent().getStringExtra(Constants.BUNDLE_NEWS_URL);

        mCommonDao = new CommonDaoImpl(this);
        mCahceMap.put(CommonCacheVo.ATY, url);
        mCahceMap.put(CommonCacheVo.DATA_TYPE, DATA_TYPE);

        mListView.setAdapter(mAdapter = new NewsDetailAdapter(this, mData = new ArrayList<News>(), new MultiItemTypeSupport<News>() {
            @Override
            public int getLayoutId(int position, News news) {
                int layoutId = 0;
                switch (news.getType()){
                    case News.NewsType.TITLE:
                        layoutId =  R.layout.news_content_title_item;
                        break;
                    case News.NewsType.SUMMARY:
                        layoutId =  R.layout.news_content_summary_item;
                        break;
                    case News.NewsType.CONTENT:
                        layoutId =  R.layout.news_content_item;
                        break;
                    case News.NewsType.IMG:
                        layoutId =  R.layout.news_content_img_item;
                        break;
                    case News.NewsType.BOLD_TITLE:
                        layoutId =  R.layout.news_content_bold_title_item;
                        break;
                }
                return layoutId;
            }

            @Override
            public int getViewTypeCount() {
                return 5;
            }

            @Override
            public int getItemViewType(int postion, News news) {
                return news.getType();
            }
        }, new NewsDetailAdapter.OnTitleReceive(){

            @Override
            public void onTitle(String title) {
                setTitle(title);
            }
        }));

        new LoadDataTask().execute(url);

    }

    @OnClick(R.id.btn_left)
    void clickLeft(){
        finish();
    }

    class LoadDataTask extends AsyncTask<String, Void, Integer>{

        @Override
        protected Integer doInBackground(String... params) {
            try {
                mData.clear();
                if(NetUtils.isNetworkConnected(Application.getInstance())) {

                    mData.addAll(mNewsItemBiz.getNews(params[0]).getNewses());

                    mCommonDao.deleteByColumns(mCahceMap);

                    CommonCacheVo cache = new CommonCacheVo();
                    cache.setAty(params[0]);
                    cache.setData_type(DATA_TYPE);
                    cache.setData(GsonUtils.getString(mData));
                    mCommonDao.save(cache);
                } else {
                    List<CommonCacheVo> list = mCommonDao.findByColumns(mCahceMap);
                    if(list != null && !list.isEmpty()){
                        CommonCacheVo cacheVo = list.get(0);
                        mData.addAll(GsonUtils.getList(cacheVo.getData(), News.class));
                    }
                }
                return 1;
            } catch (CommonException e) {
                e.printStackTrace();
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if(1 == integer.intValue()){
                mListView.onComplete();
            } else {
                mListView.onFailuer();
            }
        }
    }
}
