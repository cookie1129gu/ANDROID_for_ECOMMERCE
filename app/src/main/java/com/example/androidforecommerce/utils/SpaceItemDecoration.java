package com.example.androidforecommerce.utils;


import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

//分割线
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int bottomSpace;
    private int outSpace;

    public SpaceItemDecoration(int bottomSpace, int outSpace) {
        this.bottomSpace = bottomSpace;
        this.outSpace = outSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom=bottomSpace;
        if(parent.getChildLayoutPosition(view)%2==0)
        {
            outRect.right=outSpace;
        }else {
            outRect.left=outSpace;
        }
    }
}
