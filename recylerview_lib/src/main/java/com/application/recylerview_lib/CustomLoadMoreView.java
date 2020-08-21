package com.application.recylerview_lib;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.loadmore.BaseLoadMoreView;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

public class CustomLoadMoreView extends BaseLoadMoreView {

    private static final String TAG = "CustomLoadMoreView";

    @NotNull
    @Override
    public View getLoadComplete(@NotNull BaseViewHolder baseViewHolder) {
        Log.e(TAG,"getLoadComplete enter");
        return null;
    }

    @NotNull
    @Override
    public View getLoadEndView(@NotNull BaseViewHolder baseViewHolder) {
        Log.e(TAG,"getLoadEndView enter");
        return null;
    }

    @NotNull
    @Override
    public View getLoadFailView(@NotNull BaseViewHolder baseViewHolder) {
        Log.e(TAG,"getLoadFailView enter");
        return null;
    }

    @NotNull
    @Override
    public View getLoadingView(@NotNull BaseViewHolder baseViewHolder) {
        Log.e(TAG,"getLoadingView enter");
        return baseViewHolder.findView(R.layout.loading_view);
    }

    @NotNull
    @Override
    public View getRootView(@NotNull ViewGroup viewGroup) {
        Log.e(TAG,"getRootView enter");
        return null;
    }
}
