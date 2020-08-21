package com.application.recylerview_lib;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.recylerview_lib.databinding.ItemRecylerviewBinding;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.module.UpFetchModule;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;


/**
 *  1 ：义自己的 Adapter 继承 开源库 BaseQuickAdapter 并给泛型 ： 数据类型 和 ViewHolder
 *  2： ItemRecylerviewBinding 这个Binding 类的自动生成，只需要在 xml 中配置 <data></data> 属性即可
 */
public class AdapterDemo extends BaseQuickAdapter<ResponseData.DataInfo, BaseDataBindingHolder<ItemRecylerviewBinding>> implements LoadMoreModule, UpFetchModule {
    private final static String TAG = "RecylerViewDemoActivity";
    /**
     * 事实上是通过 onBindViewHolder 回调过来的
     * @param holder
     * @param responseData
     */
    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemRecylerviewBinding> holder, ResponseData.DataInfo responseData) {
        Log.e(TAG,"AdapterDemo convert enter position = " + holder.getAdapterPosition());
        ItemRecylerviewBinding binding = holder.getDataBinding();
        if (binding != null) {
            Log.e(TAG,"AdapterDemo responseData = " + responseData);
            binding.setInfo(responseData);
        }
    }

    public AdapterDemo() {
        super(R.layout.item_recylerview);
        Log.e(TAG,"AdapterDemo enter ");
    }
}
