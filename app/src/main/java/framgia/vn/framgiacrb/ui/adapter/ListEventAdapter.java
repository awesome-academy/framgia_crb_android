package framgia.vn.framgiacrb.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.ui.fragment.item.ItemMonth;
import framgia.vn.framgiacrb.utils.TimeUtils;

/**
 * Created by nghicv on 04/07/2016.
 */
public class ListEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_MONTH = 0;
    private static final int VIEW_TYPE_DATE = 1;
    private static final int VIEW_TYPE_EVENT = 2;
    private static final int VIEW_TYPE_TIMELINE = 3;
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
        if (object instanceof ItemMonth)
            return VIEW_TYPE_MONTH;
        if (object instanceof Date)
            return VIEW_TYPE_DATE;
        if (object instanceof Event)
            return VIEW_TYPE_EVENT;
        return VIEW_TYPE_TIMELINE;
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
            case VIEW_TYPE_TIMELINE:
                View itemViewTimeline =
                    mLayoutInflater.inflate(R.layout.item_timeline, parent, false);
                viewHolder = new TimeLineViewHolder(itemViewTimeline);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object object = mDatas.get(position);
        if (object instanceof ItemMonth) {
            ItemMonth itemMonth = (ItemMonth) object;
            String text = itemMonth.getStringMonth() + " " + itemMonth.getYear();
            MonthViewHolder monthViewHolder = (MonthViewHolder) holder;
            monthViewHolder.tvMonth.setText(text);
        } else if (object instanceof Date) {
            Date date = (Date) object;
            Date today = java.util.Calendar.getInstance().getTime();
            String dayOfMonth = (String) DateFormat.format("dd", date);
            String dayOfWeek = (String) DateFormat.format("EEE", date);
            DateViewHolder dateViewHolder = (DateViewHolder) holder;
            dateViewHolder.tvDate.setText(dayOfMonth);
            dateViewHolder.tvDay.setText(dayOfWeek);
            if (TimeUtils.toStringDate(date).compareTo(TimeUtils.toStringDate(today)) == 0) {
                dateViewHolder.tvDate
                    .setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                dateViewHolder.tvDay
                    .setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            } else {
                dateViewHolder.tvDate.setTextColor(
                    mContext.getResources().getColor(R.color.text_default_event_color));
                dateViewHolder.tvDay.setTextColor(
                    mContext.getResources().getColor(R.color.text_default_event_color));
            }
        } else if (object instanceof Event) {
            Event event = (Event) object;
            EventViewHolder eventViewHolder = (EventViewHolder) holder;
            eventViewHolder.setId(event.getId());
            eventViewHolder.tvTitleEvent.setText(event.getTitle());
            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date startDate = event.getStartTime();
            Date finishDate = event.getFinishTime();
            String startTime = format.format(startDate);
            String finishTime = format.format(finishDate);
            String time = startTime + "-" + finishTime;
            eventViewHolder.tvTime.setText(time);
            if (event.getPlace() != null) {
                eventViewHolder.tvLocation.setText(event.getPlace().getName());
                eventViewHolder.linearLayoutLocation.setVisibility(View.VISIBLE);
                eventViewHolder.cardView.setCardBackgroundColor(
                    ContextCompat.getColor(mContext, Constant.color[event.getPlace().getId() - 1]));
            } else {
                eventViewHolder.linearLayoutLocation.setVisibility(View.GONE);
                try {
                    eventViewHolder.cardView
                        .setCardBackgroundColor(Color.parseColor(event.getColorId
                            ()));
                } catch (IllegalArgumentException e) {
                    eventViewHolder.cardView
                        .setCardBackgroundColor(
                            ContextCompat.getColor(getContext(), R.color.colorPrimary));
                }
            }
        }
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
        TextView tvMonth;

        public MonthViewHolder(View itemView) {
            super(itemView);
            tvMonth = (TextView) itemView.findViewById(R.id.tv_month);
        }
    }

    class DateViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView tvDay;

        public DateViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvDay = (TextView) itemView.findViewById(R.id.tv_day);
        }
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitleEvent;
        TextView tvTime;
        CardView cardView;
        LinearLayout linearLayoutLocation;
        TextView tvLocation;
        private int mId;

        public EventViewHolder(View itemView) {
            super(itemView);
            tvTitleEvent = (TextView) itemView.findViewById(R.id.tv_title_event);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            cardView = (CardView) itemView.findViewById(R.id.card_view_item_event);
            linearLayoutLocation = (LinearLayout) itemView.findViewById(R.id.ll_location);
            tvLocation = (TextView) itemView.findViewById(R.id.tv_location);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnEventSelectedListener != null) {
                        mOnEventSelectedListener.onSelected(mId, getLayoutPosition());
                    }
                }
            });
        }

        public void setId(int id) {
            mId = id;
        }
    }

    class TimeLineViewHolder extends RecyclerView.ViewHolder {
        public TimeLineViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnEventSelectedListener {
        void onSelected(int idSelected, int position);
    }
}
