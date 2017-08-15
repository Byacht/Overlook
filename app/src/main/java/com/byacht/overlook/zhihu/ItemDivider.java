package com.byacht.overlook.zhihu;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.byacht.overlook.R;

/**
 * Created by dn on 2017/8/14.
 */

public class ItemDivider extends RecyclerView.ItemDecoration {

    private int dividerVertical;
    private int dividerHorizontal;

    public ItemDivider(Context context) {
        dividerVertical = context.getResources().getDimensionPixelSize(R.dimen.divider_vertical);
        dividerHorizontal = context.getResources().getDimensionPixelSize(R.dimen.divider_horizontal);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if (position != 0) {
            outRect.top = dividerVertical;
            outRect.bottom = dividerVertical;
            outRect.left = dividerHorizontal;
            outRect.right = dividerHorizontal;
        }
    }
}
