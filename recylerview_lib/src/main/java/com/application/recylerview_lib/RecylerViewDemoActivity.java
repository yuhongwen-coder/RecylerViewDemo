package com.application.recylerview_lib;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.listener.OnUpFetchListener;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 *  1 :
 */
public class RecylerViewDemoActivity extends AppCompatActivity implements View.OnClickListener,ResponseListener{

    private static final String DATA_SOURCE = "level_new.json";
    private static final String TAG = "RecylerViewDemoActivity";
    private static final int REFRESH_ACTIVITY = 1;
    private static final String REFRESH_DATA_SOURCE = "refresh_data_source.json";
    private RecyclerView recylerView;
    private List<ResponseData.DataInfo> dataList;
    private ResponseData data = new ResponseData();
    private MyHandler handler;
    private LinearLayout contentView;
    private AdapterDemo adapterdemo;
    private View emptyView;
    private SwipeRefreshLayout swipRefresh;
    private int count;
    private int SWIPE_REFRESH_TYPE = 1;
    private int LOAD_MODE_REFRESH_TYPE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recylerview);
        Log.e(TAG,"RecylerViewDemoActivity onCreate enter");
        recylerView = findViewById(R.id.list);
        contentView = findViewById(R.id.content_view);
        swipRefresh = findViewById(R.id.swip_refresh);
        recylerView.setLayoutManager(new LinearLayoutManager(this));
        handler = new MyHandler();
        adapterdemo = new AdapterDemo();
        recylerView.setAdapter(adapterdemo);
        emptyView = LayoutInflater.from(this).inflate(R.layout.loading_view, null,false);

        // 1: 模拟后台获取数据
        configLevelTypeList(DATA_SOURCE);

        // 2 : 加载框
        showLoadingView();

        // 3-1： TODO 框架有bug 不知道怎么触发下拉刷新的动作，所以只能结合 SwipeRefreshLayout这个控件
        adapterdemo.getUpFetchModule().setOnUpFetchListener(new OnUpFetchListener() {
            @Override
            public void onUpFetch() {
                Log.e(TAG," adapterdemo onUpFetch enter");
//                configLevelTypeList(DATA_SOURCE);
            }
        });
        // 3-2 补充3-1 的bug 我们通过 SwipeRefreshLayout这个控件来实现下拉刷新功能
        configSwipRefreshData();

        // 4：添加上拉加载更多模块
        adapterdemo.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.e(TAG," adapterdemo onLoadMore enter");
                configLevelTypeList(DATA_SOURCE);
            }
        });
    }

    private void showLoadingView() {
          //  TODO 放弃了，不知道为啥 adapterdemo.setEmptyView() 不显示，可能是框架的 bug 吧
//        adapterdemo.setEmptyView(emptyView);
//        adapterdemo.setEmptyView(R.layout.loading_view);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        // 将正在加载中的中间布局 放在 title 的下面
        contentView.addView(emptyView,1, params);
        recylerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    /**
     *
     * @param isSuccessGet true 直接显示 RecylerView 需要呈现的内容
     *                     fals, 给 根布局 contentView添加一个 errorView
     */
    private void showRecylerView(boolean isSuccessGet) {
        if (isSuccessGet) {
            emptyView.setVisibility(View.GONE);
            recylerView.setVisibility(View.VISIBLE);
        } else {
            recylerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            emptyView = LayoutInflater.from(this).inflate(R.layout.error_view,null,false);
            contentView.addView(emptyView,1, params);
        }
    }

    private void configAdapter(boolean isSuccessGet,boolean isSwipRefresh) {
        showRecylerView(isSuccessGet);
//        adapterDemo.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
//        adapterDemo.getLoadMoreModule().loadMoreToLoading();
        Log.e(TAG," configAdapter isSwipRefresh = " + isSwipRefresh);
        if (isSwipRefresh) {
            dataList = data.getDataList();
            // TODO 事实上时不能清空数据的，但是不清空，新数据又不能刷新
            adapterdemo.getData().clear();
            adapterdemo.addData(dataList);
            // 收起正在刷新的加载动画
            swipRefresh.setRefreshing(false);
        } else {
            configLoadMoreData();
        }
    }

    private void configLoadMoreData() {
        dataList = data.getDataList();
        Log.e(TAG,"onCreate data = " + dataList);
        if (dataList == null || dataList.size() ==0) {
            showRecylerView(false);
            return;
        }
        // 下面代码是模拟环境代码
        if (count == 3) {
            // 模拟3次 分页加载
            Log.e(TAG,"success count = " + count + "---服务器数据下载完毕了");
            adapterdemo.getLoadMoreModule().loadMoreEnd();
            return;
        }
        if (dataList.size() < 3) {
            // 每次请求到3个，就认为是本次加载结束，需要发起下一次请求
            Log.e(TAG,"success count = " + count + "---服务器数据下载完毕了");
            adapterdemo.getLoadMoreModule().loadMoreEnd();
            count++;
        } else {
            Log.e(TAG,"success count = " + count + "---继续向服务器下载");
            // 注意不是加载结束，而是本次数据加载结束并且还有下页数据
            adapterdemo.getLoadMoreModule().loadMoreComplete();
            count++;
        }
        adapterdemo.addData(dataList);
    }

    private void configSwipRefreshData(){
        swipRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(TAG,"configSwipRefreshData onRefresh enter");
                configLevelTypeList(REFRESH_DATA_SOURCE);
            }
        });
    }

    private void configLevelTypeList(final String assetName) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e(TAG,"configLevelTypeList sub_thread begin sleep");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                Log.e(TAG,"configLevelTypeList sub_thread  = " + Thread.currentThread().getName());
                try {
                    Log.e(TAG,"configLevelTypeList sub_thread sleep time over");
                    InputStream open = RecylerViewDemoActivity.this.getAssets().open(assetName);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(open));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result.append(line);
                    }
                    bufferedReader.close();
                    Gson gson = new Gson();
                    ResponseData data = gson.fromJson(result.toString(), ResponseData.class);
                    Message message = handler.obtainMessage();
                    message.what = REFRESH_ACTIVITY;
                    if (TextUtils.equals(assetName,REFRESH_DATA_SOURCE)) {
                        message.arg1 = SWIPE_REFRESH_TYPE;
                    } else {
                        message.arg1 = LOAD_MODE_REFRESH_TYPE;
                    }
                    message.obj = data;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Log.e(TAG,"Exception e = " +e.getMessage());
                    e.printStackTrace();
                }
            }
        },"thread1");
        thread.start();
//        try {
//            // 阻塞当前主线程，等子线程执行完毕之后，回到主线程
//            thread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }

    @Override
    public <F> void onSuccess(F response, boolean isSwipRefresh) {
        Log.e(TAG,"onSuccess enter");
        configAdapter(true, isSwipRefresh);
    }

    @Override
    public void onFail() {
        // 接口请求失败
        data = null;
        Log.e(TAG,"onFail enter");
        configAdapter(false,false);
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == REFRESH_ACTIVITY) {
                data = (ResponseData) msg.obj;
                int arg = msg.arg1;
                if (arg == SWIPE_REFRESH_TYPE) {
                    onSuccess(data,true);
                } else {
                    onSuccess(data,false);
                }
            } else {
                onFail();
            }
        }
    }
}
