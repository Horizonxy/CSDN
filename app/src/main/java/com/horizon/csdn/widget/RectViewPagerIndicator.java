package com.horizon.csdn.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.horizon.csdn.Application;

import java.util.List;

public class RectViewPagerIndicator extends LinearLayout {

    private Paint mRectPaint;
    private Rect mRect;
    private int mRectWidth;
    private int mRectHeight;
    private int mRectInitTop;

    private int mTabVisibleCount;

    private float mTranslationX;

    private ViewPager mViewPager;

    private List<String> mTabTitles;

    /** 标题正常颜色 */
    private int mNormalColor = Color.BLACK;
    /** 标题高亮颜色 */
    private int mHighLightCoiolor = 0xff1196db;

    private int mRectColor;

    public RectViewPagerIndicator(Context context) {
        this(context, null);
    }

    public RectViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTabVisibleCount = 4;
        mRectColor = Color.RED;
        mRectHeight = 3;

        mRect = new Rect();

        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setStyle(Paint.Style.FILL);
        mRectPaint.setColor(mRectColor);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int count = getChildCount();
        if(count == 0){
            return;
        }

        for (int i = 0; i < count; i++){
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            lp.weight = 0;
            lp.width = Application.getInstance().SCREENWIDTH / mTabVisibleCount;
            child.setLayoutParams(lp);

            if(mViewPager != null) {
                final int j = i;
                child.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(j);
                    }
                });
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        mRectWidth = width / mTabVisibleCount;
        mRectInitTop = getMeasuredHeight();

        setMeasuredDimension(width, mRectInitTop + mRectHeight);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();

        canvas.drawRect(mTranslationX, mRectInitTop, mTranslationX + mRectWidth, mRectInitTop + mRectHeight, mRectPaint);

        canvas.restore();

        super.dispatchDraw(canvas);
    }

    private OnPageChangeListener mPageChangeListener;

    public void setViewPager(ViewPager mViewPager, int pos){
        this.mViewPager = mViewPager;

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position, positionOffset);

                if(mPageChangeListener != null){
                    mPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                resetTextViewColor();
                highLightTextView(position);

                if(mPageChangeListener != null){
                    mPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if(mPageChangeListener != null){
                    mPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        mViewPager.setCurrentItem(pos);
        resetTextViewColor();
        highLightTextView(pos);
    }

    private void scroll(int position, float offset) {
        mTranslationX = getWidth() / mTabVisibleCount * (position + offset);
        // 容器滚动，当移动到倒数最后一个的时候，开始滚动
        if (offset > 0 && position >= (mTabVisibleCount - 2) && getChildCount() > mTabVisibleCount && position < (getChildCount() - 2)) {
            if (mTabVisibleCount != 1) {
                this.scrollTo((position - (mTabVisibleCount - 2)) * mRectWidth + (int) (mRectWidth * offset), 0);
            } else {// 为count为1时 的特殊处理
                this.scrollTo(position * mRectWidth + (int) (mRectWidth * offset), 0);
            }
        }
        invalidate();
    }

    public void setOnPageChangeListener(OnPageChangeListener pageChangeListener){
        this.mPageChangeListener = pageChangeListener;
    }

    public void setTabItemTitles(List<String> datas) {
        // 如果传入的list有值，则移除布局文件中设置的view
        if (datas != null && datas.size() > 0) {
            this.removeAllViews();
            this.mTabTitles = datas;

            for (int i = 0; i < mTabTitles.size(); i++) {
                // 添加view
                TextView child = new TextView(getContext());
                LayoutParams lp = new LayoutParams(-1, -1);
                lp.weight = 0;
                lp.width = Application.getInstance().SCREENWIDTH / mTabVisibleCount;
                child.setLayoutParams(lp);
                child.setGravity(Gravity.CENTER);
                child.setText(mTabTitles.get(i));
                child.setTextColor(mNormalColor);
                child.setTextSize(14);

                addView(child);

                if (mViewPager != null) {
                    final int j = i;
                    child.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mViewPager.setCurrentItem(j);
                        }
                    });
                }
            }
        }
    }

    public void setVisibleTabCount(int count) {
        this.mTabVisibleCount = count;
    }

    private void resetTextViewColor() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(mNormalColor);
            }
        }
    }

    protected void highLightTextView(int position) {
        View view = getChildAt(position);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(mHighLightCoiolor);
        }
    }
}
