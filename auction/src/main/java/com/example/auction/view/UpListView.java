package com.example.auction.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.auction.R;

public class UpListView extends ListView implements AbsListView.OnScrollListener {
    View footer;//底部布局
    int totalItemCount;//Item总数量
    int lastVisibleItem;//最后一个可见的Item
    boolean isLoading;//正在加载
    ILoadLister iLoadLister;//回调接口

    View header;//顶部布局
    int headerHeight;//顶部布局的高度
    int firstVisibleItem;//listview第一个可见的item ;
    boolean isRemark;//标记，当前在listview摁下的 ；
    int startY;//摁下时的Y值
    int state; // 当前的状态
    final int NONE = 0;//正常状态
    final int PULL = 1;//提示下拉刷新状态
    final int RELESE = 2;//提示释放刷新状态
    final int REFLASHING = 3;//正在刷新状态
    int scrollState;//klistview 当前的滚动状态

    public UpListView(Context context) {
        super(context);
        initView(context);
    }

    public UpListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public UpListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public UpListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    /*
     * 添加底部加载布局到listview
     * */
    private void initView(Context context) {
        //设置滚动事件
        this.setOnScrollListener(this);

        LayoutInflater inflater = LayoutInflater.from(context);
        //加载底部布局
        footer = inflater.inflate(R.layout.footer_layout, null);
        //底部布局初始不可见
        footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
        //将布局加载到listview上
        this.addFooterView(footer);

        //加载顶部布局
        header = inflater.inflate(R.layout.header_layout, null);
        //获取高度前，通知父布局
        measureView(header);
        //获得header的高度
        headerHeight = header.getMeasuredHeight();
        //设置header的上边距是高度的负值，隐藏header
        topPadding(-headerHeight);
        this.addHeaderView(header);
    }

    //通知父布局，占用的宽，高；
    private void measureView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, params.width);
        int height;
        int tempHeight = params.height;
        if (tempHeight > 0) {
            height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, height);
    }

    //设置header布局的上边距
    private void topPadding(int topPadding) {
        header.setPadding(header.getPaddingLeft(), topPadding,
                header.getPaddingRight(), header.getPaddingBottom());
        header.invalidate();
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.lastVisibleItem = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;
        this.firstVisibleItem = firstVisibleItem;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
        if (totalItemCount == lastVisibleItem
                && scrollState == SCROLL_STATE_IDLE) {
            //滚动到底部，加载底部布局
            if (!isLoading) {
                isLoading = true;
                footer.findViewById(R.id.load_layout).setVisibility(View.VISIBLE);
                //加载数据
                iLoadLister.onLoad();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0) {
                    isRemark = true;
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (state == RELESE) {
                    //如果状态是在提示释放时释放，状态变成刷新状态
                    state = REFLASHING;
                    reflashViewByState();
                    //加载最新数据
                    iLoadLister.onReflash();
                } else if (state == PULL) {
                    state = NONE;
                    isRemark = false;
                    reflashViewByState();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    //根绝下拉的状态改变提示信息
    private void reflashViewByState() {
        TextView tip = (TextView) header.findViewById(R.id.tip);
        ProgressBar progressBar = (ProgressBar) header.findViewById(R.id.progress);
        ImageView img_arrow = (ImageView) header.findViewById(R.id.arrow);

        //箭头方向改变的动画效果
        RotateAnimation animation = new RotateAnimation(0, 180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        RotateAnimation animation1 = new RotateAnimation(180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation1.setDuration(500);
        animation1.setFillAfter(true);

        switch (state) {
            case NONE:
                img_arrow.clearAnimation();
                topPadding(-headerHeight);
                break;
            case PULL:
                img_arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tip.setText("下拉刷新");
                img_arrow.clearAnimation();
                img_arrow.setAnimation(animation1);
                break;
            case RELESE:
                tip.setText("释放刷新");
                img_arrow.setVisibility(View.VISIBLE);
                img_arrow.clearAnimation();
                img_arrow.setAnimation(animation);
                progressBar.setVisibility(View.GONE);
                break;
            case REFLASHING:
                topPadding(50);
                img_arrow.clearAnimation();
                img_arrow.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                tip.setText("正在刷新...");
                break;
        }
    }

    //顶部数据刷新完毕
    public void reflashComplete() {
        state = NONE;
        isRemark = false;
        reflashViewByState();
    }

    //顶部下滑，移动过程的操作
    private void onMove(MotionEvent ev) {
        if (!isRemark) {
            return;
        }
        int tempY = (int) ev.getY();
        //下拉移动的距离
        int space = tempY - startY;
        //设置下拉时的顶部布局高度
        int topPadding = space - headerHeight;
        switch (state) {
            case NONE:
                //下拉，状态变成下拉状态
                if (space > 0) {
                    state = PULL;
                    reflashViewByState();
                }
                break;
            case PULL:
                topPadding(topPadding);
                //下拉的高度大于一定的高度，并且listview在滚动，提示释放
                if (space > headerHeight
                        && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    state = RELESE;
                    reflashViewByState();

                }
                break;
            case RELESE:
                topPadding(topPadding);
                //如果小于一定高度，状态变成下拉
                if (space < headerHeight + 30) {
                    state = PULL;
                    reflashViewByState();
                } else if (space <= 0) {
                    //下拉高度小于0，状态变成正常状态
                    state = NONE;
                    isRemark = false;
                    reflashViewByState();
                }
                break;
        }
    }

    //底部加载更多数据完毕
    public void loadComplete() {
        isLoading = false;
        footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
    }

    public void setInterface(ILoadLister iLoadLister) {
        this.iLoadLister = iLoadLister;
    }


    //加载更多数据的回调接口
    public interface ILoadLister {
        //加载更多数据
        public void onLoad();

        //加载最新数据
        public void onReflash();
    }
}
