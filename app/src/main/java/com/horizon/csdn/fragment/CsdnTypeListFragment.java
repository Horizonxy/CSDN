package com.horizon.csdn.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.horizon.csdn.Application;
import com.horizon.csdn.R;
import com.horizon.csdn.adapter.CsdnListAdapter;
import com.horizon.csdn.adapter.quickadapter.MultiItemTypeSupport;
import com.horizon.csdn.db.CommonDaoImpl;
import com.horizon.csdn.util.GsonUtils;
import com.horizon.csdn.util.NetUtils;
import com.horizon.csdn.vo.CommonCacheVo;
import com.horizon.csdn.widget.AutoLoadListView;
import com.horizon.csdn.widget.InitializeListView;
import com.zhy.bean.CommonException;
import com.zhy.bean.NewsItem;
import com.zhy.biz.NewsItemBiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsdnTypeListFragment extends Fragment {

    private static final String TYPE = "type";
    private int mType;

    private CommonDaoImpl mCommonDao;
    private NewsItemBiz mNewsItemBiz;

    private InitializeListView mListView;
    private int mPageNo;
    private ArrayList<NewsItem> mData;
    private CsdnListAdapter mAdapter;

    private Map<String, Object> mCahceMap = new HashMap<String, Object>();
    private String ATY = "csdn_news_";
    private static final String DATA_TYPE = "csdn_news_list";

    public static CsdnTypeListFragment newInstance(int type) {
        CsdnTypeListFragment fragment = new CsdnTypeListFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(TYPE);
        }
        ATY = ATY + mType;

        mPageNo = 1;
        mCommonDao = new CommonDaoImpl(getContext());
        mNewsItemBiz = new NewsItemBiz();

        mCahceMap.put(CommonCacheVo.ATY, ATY);
        mCahceMap.put(CommonCacheVo.DATA_TYPE, DATA_TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_csdn_type_list, container, false);

        mListView = (InitializeListView) view.findViewById(R.id.news_page_list);
        mListView.setAutoLoadListener(new AutoLoadListener());
        mListView.setDivider(getResources().getColor(R.color.background), 2);
        mListView.setAdapter(mAdapter = new CsdnListAdapter(getContext(), mData = new ArrayList<NewsItem>(), new MultiItemTypeSupport<NewsItem>() {
            @Override
            public int getLayoutId(int position, NewsItem newsItem) {
                if(TextUtils.isEmpty(newsItem.getImgLink())){
                    return R.layout.item_csdn_news_list_no_banner;
                } else {
                    return R.layout.item_csdn_news_list;
                }
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public int getItemViewType(int postion, NewsItem newsItem) {
                if(TextUtils.isEmpty(newsItem.getImgLink())){
                    return -1;
                } else {
                    return 1;
                }
            }
        }));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        new LoadDataTask().execute();
    }


    class AutoLoadListener implements AutoLoadListView.OnAutoLoadListener {

        @Override
        public void onLoading() {
            new LoadDataTask().execute();
        }
    }

    class LoadDataTask extends AsyncTask<Void, Void, Integer>{

        @Override
        protected Integer doInBackground(Void... params) {
            return getNewsList();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer.intValue() == -1) {
                mListView.onFinish();
            } else {
                mListView.onComplete();
            }
        }
    }

    private int getNewsList() {
        try {
            if(mPageNo == 1){
                mData.clear();
            }

            if(NetUtils.isNetworkConnected(Application.getInstance())) {
                List<NewsItem> list = mNewsItemBiz.getNewsItems(mType, mPageNo);
                if (list != null && !list.isEmpty()) {
                    mData.addAll(list);
                } else if(mPageNo > 1) {
                    return -1;
                }

                if (mPageNo == 1) {
                    Map<String, Object> deleteMap = new HashMap<String, Object>();
                    deleteMap.put(CommonCacheVo.ATY, ATY);
                    deleteMap.put(CommonCacheVo.DATA_TYPE, DATA_TYPE);
                    mCommonDao.deleteByColumns(deleteMap);
                }

                CommonCacheVo cacheVo = new CommonCacheVo();
                cacheVo.setAty(ATY);
                cacheVo.setData_page_no(mPageNo);
                cacheVo.setData_type(DATA_TYPE);
                cacheVo.setData(GsonUtils.getString(list));
                mCommonDao.save(cacheVo);
            } else {
                mCahceMap.put(CommonCacheVo.DATA_PAGE_NO, mPageNo);
                List<CommonCacheVo> cacheList = mCommonDao.findByColumns(mCahceMap);
                if(cacheList != null && !cacheList.isEmpty()){
                    CommonCacheVo cache = cacheList.get(0);
                    List<NewsItem> list = GsonUtils.getList(cache.getData(), NewsItem.class);
                    if (!list.isEmpty()) {
                        mData.addAll(list);
                    }
                } else if(mPageNo > 1) {
                    return -1;
                }
            }
            mPageNo++;
        } catch (CommonException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
