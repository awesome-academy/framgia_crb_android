package framgia.vn.framgiacrb.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.activity.DetailActivity;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.utils.TimeUtils;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by framgia on 26/07/2016.
 */
public class SearchEventAdapter extends RealmRecyclerViewAdapter<Event, SearchEventAdapter
    .EventViewHolder> {
    public final Activity mActivity;
    public OrderedRealmCollection<Event> data;
    public SearchEventAdapter(Activity activity, OrderedRealmCollection<Event> data) {
        super(activity, data, true);
        this.mActivity = activity;
        this.data = data;
    }

    @Override
    public SearchEventAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_search_result, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchEventAdapter.EventViewHolder holder, int position) {
        Event obj = getData().get(position);
        holder.day.setText(TimeUtils.toDay(obj.getStartTime()));
        holder.month.setText(TimeUtils.toMonth(obj.getStartTime()));
        holder.content.setText(obj.getTitle());
        holder.content.setBackgroundColor(
            mActivity.getResources().getColor(Constant.color[obj.getColorId() - 1]));
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView day;
        public TextView content;
        public TextView month;
        public CardView cardView;

        public EventViewHolder(View itemView) {
            super(itemView);
            day = (TextView) itemView.findViewById(R.id.text_day);
            month = (TextView) itemView.findViewById(R.id.text_month);
            content = (TextView) itemView.findViewById(R.id.text_title);
            cardView = (CardView) itemView.findViewById(R.id.card_view_search);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mActivity, DetailActivity.class);
            intent.putExtra(Constant.ID_KEY, data.get(getAdapterPosition()).getId());
            mActivity.startActivity(intent);
        }
    }
}
