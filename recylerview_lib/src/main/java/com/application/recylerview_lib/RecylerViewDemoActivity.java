package com.application.recylerview_lib;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 *  1 :
 */
public class RecylerViewDemoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DATA_SOURCE = "level.json";
    private static final String TAG = "RecylerViewDemoActivity";
    private static final int REFRESH_ACTIVITY = 1;
    private RecyclerView recylerView;
    private List<ResponseData.DataInfo> dataList;
    private ResponseData data = new ResponseData();
    private MyHandler handler;
    private LinearLayout contentView;
    private AdapterDemo adapterdemo;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recylerview);
        Log.e(TAG,"RecylerViewDemoActivity onCreate enter");
        recylerView = findViewById(R.id.list);
        contentView = findViewById(R.id.content_view);
        recylerView.setLayoutManager(new LinearLayoutManager(this));
        handler = new MyHandler();
        adapterdemo = new AdapterDemo();
        emptyView = LayoutInflater.from(this).inflate(R.layout.loading_view, null,false);

        // 1: 模拟后台获取数据
        configLevelTypeList(DATA_SOURCE);

        // 2 : 加载框
        showLoadingView();

        // 3： 主线程被唤醒刷新页面
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

    private void configAdapter(boolean isSuccessGet) {
        showRecylerView(isSuccessGet);
        dataList = data.getDataList();
        Log.e(TAG,"onCreate data = " + dataList);
        recylerView.setAdapter(adapterdemo);
        // 源码存在 notifyDataSetChanged 刷新的操作
        adapterdemo.addData(dataList);
//        adapterDemo.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
//        adapterDemo.getLoadMoreModule().loadMoreToLoading();
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
                    message.obj = data;
                    handler.sendMessage(message);
                } catch (Exception e) {
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

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == REFRESH_ACTIVITY) {
                data = (ResponseData) msg.obj;
                configAdapter(true);
            }
        }
    }
}
