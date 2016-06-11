package com.horizon.csdn.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import com.horizon.csdn.R;


/**
 * 自动加载listview  包含初始加载动画、没数据时view
 * @author 蒋先明
 * @date 2016年4月14日
 */
public class InitializeListView extends FrameLayout {

	private AutoLoadListView mAutoListView;
	private BaseAdapter mListAdapter;
	private View mEmptyView;
	private View mFailure;
	private View mInitializeView;
	private int mDividerHeight;

	public InitializeListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.InitializeListView);
		mDividerHeight = attributes.getDimensionPixelOffset(R.styleable.InitializeListView_list_dividerHeight, mDividerHeight);
		attributes.recycle();

		init(context);
	}

	public InitializeListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public InitializeListView(Context context) {
		this(context, null);
	}

	@SuppressLint("InflateParams")
	private void init(Context context) {
		FrameLayout layout = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.view_initialize_listview, null);
		LayoutParams lp = new LayoutParams(-1, -1);
		setLayoutParams(lp);

		mAutoListView = (AutoLoadListView) layout.findViewById(R.id.lv_autoload);
		mAutoListView.setDividerHeight(mDividerHeight);
		mEmptyView = layout.findViewById(R.id.empty);
		mInitializeView = layout.findViewById(R.id.init_load);
		mFailure = layout.findViewById(R.id.failure);

		mAutoListView.setLayoutParams(lp);
		mEmptyView.setLayoutParams(lp);
		mInitializeView.setLayoutParams(lp);

		this.addView(layout);
	}

	private void showEmpty() {
		if (mEmptyView.getVisibility() == View.GONE) {
			mEmptyView.setVisibility(View.VISIBLE);
		}
		if (mFailure.getVisibility() == View.VISIBLE) {
			mFailure.setVisibility(View.GONE);
		}
		if (mAutoListView.getVisibility() == View.VISIBLE) {
			mAutoListView.setVisibility(View.GONE);
		}
	}

	private void showFailure() {
		if (mEmptyView.getVisibility() == View.VISIBLE) {
			mEmptyView.setVisibility(View.GONE);
		}
		if (mFailure.getVisibility() == View.GONE) {
			mFailure.setVisibility(View.VISIBLE);
		}
		if (mAutoListView.getVisibility() == View.VISIBLE) {
			mAutoListView.setVisibility(View.GONE);
		}
	}

	private void showListView() {
		if (mEmptyView.getVisibility() == View.VISIBLE) {
			mEmptyView.setVisibility(View.GONE);
		}
		if (mFailure.getVisibility() == View.VISIBLE) {
			mFailure.setVisibility(View.GONE);
		}
		if (mAutoListView.getVisibility() == View.GONE) {
			mAutoListView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 设置无数据是view
	 *
	 * @param mEmptyView
	 * @author 蒋先明
	 * @date 2016年4月14日
	 */
	public void setEmptyView(View mEmptyView) {
		int visibility = this.mEmptyView.getVisibility();
		mEmptyView.setVisibility(visibility);
		LayoutParams lp = new LayoutParams(-1, -1);
		mEmptyView.setLayoutParams(lp);

		this.mEmptyView = mEmptyView;
	}

	/**
	 * 这是第一次加载view
	 *
	 * @param mInitializeView
	 * @author 蒋先明
	 * @date 2016年4月14日
	 */
	public void setInitializeView(View mInitializeView) {
		int visibility = this.mInitializeView.getVisibility();
		mInitializeView.setVisibility(visibility);
		LayoutParams lp = new LayoutParams(-1, -1);
		mInitializeView.setLayoutParams(lp);

		this.mInitializeView = mInitializeView;
	}

	public AutoLoadListView.State getState() {
		return mAutoListView.mState;
	}

	public boolean isFinish() {
		return AutoLoadListView.State.FINISH == mAutoListView.mState;
	}

	/**
	 * 加载失败
	 *
	 * @author 蒋先明
	 * @date 2016年4月13日
	 */
	public void onFailuer() {
		goneLoadView();

		if (mListAdapter != null && mListAdapter.getCount() == 0) {
			showFailure();
		} else {
			mAutoListView.onFailuer();
		}
	}

	private void goneLoadView() {
		if (mInitializeView.getVisibility() == View.VISIBLE) {
			mInitializeView.setVisibility(View.GONE);
		}
	}

	/**
	 * 所有加载完成  暂无更多数据
	 *
	 * @author 蒋先明
	 * @date 2016年4月13日
	 */
	public void onFinish() {
		goneLoadView();

		if (mListAdapter != null && mListAdapter.getCount() == 0) {
			showFailure();
		} else {
			mAutoListView.onFinish();
		}
	}

	/**
	 * 单次加载完成
	 *
	 * @author 蒋先明
	 * @date 2016年4月13日
	 */
	public void onComplete() {
		mListAdapter.notifyDataSetChanged();

		mAutoListView.onComplete();

		goneLoadView();

		if (mListAdapter != null && mListAdapter.getCount() == 0) {
			showEmpty();
		} else {
			showListView();
		}
	}

	public void setDivider(int color, int height) {
		mAutoListView.setDivider(new ColorDrawable(color));
		mAutoListView.setDividerHeight(height);
	}

	/**
	 * 设置listview adapter
	 *
	 * @param adapter
	 * @author 蒋先明
	 * @date 2016年4月14日
	 */
	public void setAdapter(BaseAdapter adapter) {
		mAutoListView.setAdapter(null);
		mListAdapter = adapter;
		mAutoListView.setAdapter(adapter);
	}

	public AutoLoadListView getListView() {
		return mAutoListView;
	}

	/**
	 * 设置自动加载监听
	 *
	 * @param listener
	 * @author 蒋先明
	 * @date 2016年4月14日
	 */
	public void setAutoLoadListener(AutoLoadListView.OnAutoLoadListener listener) {
		mAutoListView.setAutoLoadListener(listener);
	}

}
