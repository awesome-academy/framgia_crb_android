package framgia.vn.framgiacrb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.fragment.item.Date;
import framgia.vn.framgiacrb.fragment.item.Month;

/**
 * Created by nghicv on 04/07/2016.
 */
public class ListEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MONTH = 0;
    private static final int VIEW_TYPE_DATE = 1;
    private static final int VIEW_TYPE_EVENT = 2;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Object> mDatas;
    private OnEventSelectedListener mOnEventSelectedListener;

    public void setOnEventSelectedListener(OnEventSelectedListener onEventSelectedListener) {
        mOnEventSelectedListener = onEventSelectedListener;
    }

    public ListEventAdapter(Context context, List<Object> datas) {
        mContext = context;
        mDatas = datas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        Object object = mDatas.get(position);
        if(object instanceof Month)
            return VIEW_TYPE_MONTH;
        if (object instanceof Date)
            return VIEW_TYPE_DATE;

        return VIEW_TYPE_EVENT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_MONTH:
                View itemViewMonth = mLayoutInflater.inflate(R.layout.item_month, parent, false);
                viewHolder = new MonthViewHolder(itemViewMonth);
                break;
            case VIEW_TYPE_DATE:
                View itemViewDate = mLayoutInflater.inflate(R.layout.item_date, parent, false);
                viewHolder = new DateViewHolder(itemViewDate);
                break;

            case VIEW_TYPE_EVENT:
                View itemmViewEvent = mLayoutInflater.inflate(R.layout.item_event, parent, false);
                viewHolder = new EventViewHolder(itemmViewEvent);
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void onItemDismiss(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext() {
        return mContext;
    }

    public List<Object> getDatas() {
        return mDatas;
    }

    class MonthViewHolder extends RecyclerView.ViewHolder {

        public MonthViewHolder(View itemView) {
            super(itemView);
        }
    }

    class DateViewHolder extends RecyclerView.ViewHolder {

        public DateViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {


        public EventViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnEventSelectedListener != null) {
                        mOnEventSelectedListener.onSelected(getLayoutPosition());
                    }
                }
            });
        }
    }

    public interface OnEventSelectedListener {
        void onSelected (int position);
    }
}
