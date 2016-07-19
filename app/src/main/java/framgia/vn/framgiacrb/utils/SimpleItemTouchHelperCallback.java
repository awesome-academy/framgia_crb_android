package framgia.vn.framgiacrb.utils;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.adapter.ListEventAdapter;

/**
 * Created by nghicv on 05/07/2016.
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ListEventAdapter mAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.ViewHolder viewHolder;
    private RecyclerView.ViewHolder target;

    public SimpleItemTouchHelperCallback(ListEventAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        if (viewHolder instanceof ListEventAdapter.EventViewHolder) {
            return makeMovementFlags(dragFlags, swipeFlags);
        }
        return 0;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {
        if (viewHolder instanceof ListEventAdapter.EventViewHolder) {

            DialogUtils.showAlertAction(mAdapter.getContext(), R.string.message_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;
        Paint p = new Paint();
        Drawable drawable;
        if (dX > 0) {
            if (Build.VERSION.SDK_INT >= 21) {
                drawable = mAdapter.getContext().getResources().getDrawable(R.drawable.ic_delete_grey600_24dp, null);
            } else {
                drawable = mAdapter.getContext().getResources().getDrawable(R.drawable.ic_delete_grey600_24dp);
            }
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            c.drawBitmap(bitmap,(float) itemView.getWidth()/2, (float) itemView.getTop(), null);
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                drawable = mAdapter.getContext().getResources().getDrawable(R.drawable.ic_delete_grey600_24dp, null);
            } else {
                drawable = mAdapter.getContext().getResources().getDrawable(R.drawable.ic_delete_grey600_24dp);
            }
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            c.drawBitmap(bitmap,(float) itemView.getWidth()/2, (float) itemView.getTop(), null);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}