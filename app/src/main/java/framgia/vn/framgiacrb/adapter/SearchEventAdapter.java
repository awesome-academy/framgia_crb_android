package framgia.vn.framgiacrb.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.data.model.Event;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by framgia on 26/07/2016.
 */
public class SearchEventAdapter extends RealmRecyclerViewAdapter<Event, SearchEventAdapter
    .EventViewHolder> {
    private final Activity mActivity;
    public SearchEventAdapter(Activity activity, OrderedRealmCollection<Event> data) {
        super(activity, data, true);
        this.mActivity = activity;
    }

    @Override
    public SearchEventAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchEventAdapter.EventViewHolder holder, int position) {
        Event obj = getData().get(position);
        Date startDate = obj.getStartDate();
        String timeDay = Integer.toString(startDate.getDay()) + " / " + Integer.toString
            (startDate.getMonth());
        holder.time.setText(timeDay);
        holder.content.setText(obj.getDescription());
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView time;
        public TextView content;
        public EventViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            content = (TextView) itemView.findViewById(R.id.tv_title_event);
        }
    }
}
