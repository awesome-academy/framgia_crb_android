package framgia.vn.framgiacrb.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.activity.DetailActivity;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.utils.SearchUtil;
import framgia.vn.framgiacrb.utils.TimeUtils;
import framgia.vn.framgiacrb.utils.Utils;
import io.realm.OrderedRealmCollection;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by framgia on 26/07/2016.
 */
public class SearchEventAdapter extends RealmRecyclerViewAdapter<Event, SearchEventAdapter
    .EventViewHolder> {
    public static final int TYPE_YEAR = 0;
    public static final int TYPE_EVENT = 1;
    public static final int TEXT_YEAR_SIZE = 30;
    public static final int TEXT_CONTENT_SIZE = 15;
    public final Activity mActivity;
    public RealmList<Event> data = new RealmList<>();

    public SearchEventAdapter(Activity activity, OrderedRealmCollection<Event> data) {
        super(activity, data, true);
        this.mActivity = activity;
        if (data.size() > 0) {
            this.data = SearchUtil.editListDataSearch(data);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public SearchEventAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_search_result, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchEventAdapter.EventViewHolder holder, int position) {
        Event obj = data.get(position);
        if (obj.getTitle().equals(SearchUtil.DEFINE_YEAR)) {
            holder.type = TYPE_YEAR;
            holder.content.setText(obj.getDescription());
            holder.content.setBackgroundColor(Utils.getColor(mActivity, R.color.bg_default));
            holder.content.setTextColor(mActivity.getResources().getColor(R.color
                .text_default_event_color));
            holder.content.setTextSize(TEXT_YEAR_SIZE);
            holder.day.setText("");
            holder.month.setText("");
        } else {
            holder.type = TYPE_EVENT;
            if (position > 0 && (!data.get(position - 1).getTitle().equals(SearchUtil.DEFINE_YEAR))
                && (TimeUtils.compareDate(obj.getStartTime(), data.get(position - 1).getStartTime())
            )) {
                holder.day.setText("");
                holder.month.setText("");
            } else {
                holder.day.setText(TimeUtils.toDay(obj.getStartTime()));
                holder.month.setText(TimeUtils.toMonth(obj.getStartTime()));
            }
            String content = obj.getTitle() + Constant.LINE_BREAK +
                TimeUtils.toStringTime(obj.getStartTime())
                + Constant.AMOUNT_DEVIDE
                + TimeUtils.toStringTime(obj.getFinishTime())
                + (obj.getPlace() == null ? "" : Constant.LINE_BREAK + obj.getPlace().getName());
            holder.content.setText(content);
            holder.content.setTextColor(mActivity.getResources().getColor(
                R.color.white));
            if (obj.getPlace() != null) {
                holder.content.setBackgroundColor(
                    ContextCompat.getColor(mActivity, Constant.color[obj.getPlace().getId() - 1]));
            } else {
                try {
                    holder.content.setBackgroundColor(Color.parseColor(obj.getColorId()));
                } catch (IllegalArgumentException e) {
                    holder.content
                        .setBackgroundColor(
                            ContextCompat.getColor(mActivity, R.color.colorPrimary));
                }
            }
            holder.content.setTextSize(TEXT_CONTENT_SIZE);
        }
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView day;
        public TextView content;
        public TextView month;
        public int type = TYPE_EVENT;

        public EventViewHolder(View itemView) {
            super(itemView);
            day = (TextView) itemView.findViewById(R.id.text_day);
            month = (TextView) itemView.findViewById(R.id.text_month);
            content = (TextView) itemView.findViewById(R.id.text_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (type == TYPE_EVENT) {
                Intent intent = new Intent(mActivity, DetailActivity.class);
                intent.putExtra(Constant.ID_KEY, data.get(getAdapterPosition()).getId());
                mActivity.startActivity(intent);
            }
        }
    }
}
