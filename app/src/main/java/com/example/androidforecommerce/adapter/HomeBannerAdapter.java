package com.example.androidforecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.example.androidforecommerce.R;
import com.example.androidforecommerce.utils.Utils;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.List;

public class HomeBannerAdapter extends DelegateAdapter.Adapter<HomeBannerAdapter.ActViewHolder> {

    private Context context;
    private LayoutHelper layoutHelper;
    private List<Integer> imgs;//轮播图；

    public HomeBannerAdapter(){}
    public HomeBannerAdapter(List<Integer> imgs, Context context, LayoutHelper layoutHelper) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.imgs=imgs;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.layoutHelper;
    }

    @Override
    public ActViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ActViewHolder(LayoutInflater.from(context).inflate(R.layout.fragement_youth_banner,null,false));

    }

    @Override
    public void onBindViewHolder(final ActViewHolder holder, final int position) {
        holder.banner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.getScreenHeight(context) /4));
        holder.banner.setImages(imgs);
        holder.banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
        holder.banner.start();
    }

    @Override
    public int getItemCount() {
        return 1;
    }


    protected static class ActViewHolder extends RecyclerView.ViewHolder{

        private Banner banner;
        public ActViewHolder(View itemView) {
            super(itemView);
            banner=(Banner)itemView.findViewById(R.id.banner);
        }
    }
}
