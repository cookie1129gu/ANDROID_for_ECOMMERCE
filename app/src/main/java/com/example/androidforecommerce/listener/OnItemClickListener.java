package com.example.androidforecommerce.listener;

import android.view.View;

public interface OnItemClickListener {

    void onItemClick(View view,int pos);

    void onItemLongClick(View v, int pos);
}
