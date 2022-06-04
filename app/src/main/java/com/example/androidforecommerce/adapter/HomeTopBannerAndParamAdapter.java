package com.example.androidforecommerce.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.example.androidforecommerce.R;
import com.example.androidforecommerce.pojo.Param;

import java.util.List;

/*
 * 头部adapter
 * */

public class HomeTopBannerAndParamAdapter extends DelegateAdapter.Adapter<HomeTopBannerAndParamAdapter.BannerAndParamViewHolder> {
    private List<Param> mData;
    private Context context;
    private LayoutHelper layoutHelper;

    public HomeTopBannerAndParamAdapter(List<Param> mData,Context context,LayoutHelper layoutHelper){
        this.mData=mData;
        this.context=context;
        this.layoutHelper=layoutHelper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.layoutHelper;
    }

    /*
     *创建
     */
    @NonNull
    @Override
    public HomeTopBannerAndParamAdapter.BannerAndParamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.fragment_home_params_list_item,null,false);
        return new BannerAndParamViewHolder(view);
    }

    /*绑定数据*/
    @Override
    public void onBindViewHolder(HomeTopBannerAndParamAdapter.BannerAndParamViewHolder holder, int position) {
        holder.tv.setText(mData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class BannerAndParamViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;
        public BannerAndParamViewHolder(View itemView) {
            super(itemView);
            tv=(TextView)itemView.findViewById(R.id.item_tv);
        }
    }
}