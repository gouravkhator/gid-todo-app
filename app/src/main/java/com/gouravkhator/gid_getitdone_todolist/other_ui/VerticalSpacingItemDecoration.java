package com.gouravkhator.gid_getitdone_todolist.other_ui;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;
    private final int horizontalSpace;

    public VerticalSpacingItemDecoration(int verticalSpaceHeight,int horizontalSpace) {
        this.verticalSpaceHeight = verticalSpaceHeight;
        this.horizontalSpace = horizontalSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {

        outRect.left = horizontalSpace;
        outRect.bottom = verticalSpaceHeight;
        outRect.right = horizontalSpace;
    }
}

