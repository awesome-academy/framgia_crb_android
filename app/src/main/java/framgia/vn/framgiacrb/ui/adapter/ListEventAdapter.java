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
            ((MonthViewHolder) holder).bindView((ItemMonth) object);
            return;
        }
        if (object instanceof Date) {
            ((DateViewHolder) holder).bindView((Date) object);
            return;
        }
        if (object instanceof Event) {
            ((EventViewHolder) holder).bindView((Event) object);
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

    class MonthViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMonth;

        public MonthViewHolder(View itemView) {
            super(itemView);
            tvMonth = (TextView) itemView.findViewById(R.id.tv_month);
        }

        public void bindView(ItemMonth itemMonth) {
            String text = itemMonth.getStringMonth() + " " + itemMonth.getYear();
            tvMonth.setText(text);
        }
    }

    class DateViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private TextView tvDay;

        public DateViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvDay = (TextView) itemView.findViewById(R.id.tv_day);
        }

        public void bindView(Date date) {
            Date today = java.util.Calendar.getInstance().getTime();
            String dayOfMonth = (String) DateFormat.format(TimeUtils.DAY_FORMAT_COUPLE, date);
            String dayOfWeek =
                (String) DateFormat.format(TimeUtils.DAY_IN_WEEK_STRING_FORMAT, date);
            tvDate.setText(dayOfMonth);
            tvDay.setText(dayOfWeek);
            if (TimeUtils.toStringDate(date).compareTo(TimeUtils.toStringDate(today)) == 0) {
                tvDate
                    .setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                tvDay
                    .setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            } else {
                tvDate.setTextColor(
                    mContext.getResources().getColor(R.color.text_default_event_color));
                tvDay.setTextColor(
                    mContext.getResources().getColor(R.color.text_default_event_color));
            }
        }
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitleEvent;
        private TextView tvTime;
        private CardView cardView;
        private LinearLayout linearLayoutLocation;
        private TextView tvLocation;
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

        public void bindView(Event event) {
            setId(event.getId());
            tvTitleEvent.setText(event.getTitle());
            if (event.isAllDay()) {
                tvTime.setVisibility(View.GONE);
            } else {
                SimpleDateFormat format =
                    new SimpleDateFormat(TimeUtils.TIME_FORMAT, Locale.getDefault());
                Date startDate = event.getStartTime();
                Date finishDate = event.getFinishTime();
                String startTime = format.format(startDate);
                String finishTime = format.format(finishDate);
                String time = startTime + "-" + finishTime;
                tvTime.setVisibility(View.VISIBLE);
                tvTime.setText(time);
            }
            if (event.getPlace() != null) {
                tvLocation.setText(event.getPlace().getName());
                linearLayoutLocation.setVisibility(View.VISIBLE);
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(mContext, Constant.color[event.getPlace().getId() - 1]));
            }
            if (event.getPlace() == null && event.getColorId() == null) {
                linearLayoutLocation.setVisibility(View.GONE);
                cardView
                    .setCardBackgroundColor(
                        ContextCompat.getColor(getContext(), R.color.colorPrimary));
            }
            if (event.getPlace() == null && event.getColorId() != null) {
                linearLayoutLocation.setVisibility(View.GONE);
                try {
                    cardView
                        .setCardBackgroundColor(Color.parseColor(event.getColorId
                            ()));
                } catch (IllegalArgumentException e) {
                    cardView
                        .setCardBackgroundColor(
                            ContextCompat.getColor(getContext(), R.color.colorPrimary));
                }
            }
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
