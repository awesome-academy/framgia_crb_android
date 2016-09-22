package framgia.vn.framgiacrb.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.fragment.item.ItemLeftMenu;

/**
 * Created by lucky_luke on 7/20/2016.
 */
public class ListMenuAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_LABEL = 2;
    private static final int VIEW_TYPE_CALENDAR = 3;
    private ArrayList<ItemLeftMenu> mListMenuItem;
    private Context mContext;

    public ListMenuAdapter(Context context, ArrayList<ItemLeftMenu> items) {
        mContext = context;
        mListMenuItem = items;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return VIEW_TYPE_HEADER;
        if (position == 4)
            return VIEW_TYPE_LABEL;
        if (position >= 5 && position < mListMenuItem.size() - 1) {
            return VIEW_TYPE_CALENDAR;
        }
        return VIEW_TYPE_ITEM;
    }

    @Override
    public int getCount() {
        return this.mListMenuItem.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View v;
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER:
                v = LayoutInflater.from(this.mContext).inflate(R.layout.header_drawer, parent, false);
                ItemLeftMenu header = this.mListMenuItem.get(position);
                holder.titleTextView = (TextView) v.findViewById(R.id.user_name);
                holder.iconImageView = (ImageView) v.findViewById(R.id.profile_image);
                holder.emailTextView = (TextView) v.findViewById(R.id.email);
                holder.iconImageView.setImageResource(header.getImageResource());
                holder.titleTextView.setText(header.getTitle());
                holder.emailTextView.setText(header.getEmail());
                break;

            case VIEW_TYPE_LABEL:
                v = LayoutInflater.from(this.mContext).inflate(R.layout.item_label_listview_menu, parent, false);
                holder.titleTextView = (TextView) v.findViewById(R.id.label);
                holder.titleTextView.setText(this.mListMenuItem.get(position).getTitle());
                break;
            case VIEW_TYPE_CALENDAR:
                v = LayoutInflater.from(mContext).inflate(R.layout.item_menu_calendar, parent, false);
                holder.titleTextView = (TextView) v.findViewById(R.id.title_textview);
                holder.titleTextView.setText(mListMenuItem.get(position).getTitle());
                holder.iconImageView = (ImageView) v.findViewById(R.id.icon_imageview);
                holder.iconImageView.setImageResource(this.mListMenuItem.get(position).getImageResource());
                holder.appCompatCheckBox = (AppCompatCheckBox) v.findViewById(R.id.cb_selected_calendar);
                holder.appCompatCheckBox.setChecked(mListMenuItem.get(position).isSelected());
                break;

            default:
                v = LayoutInflater.from(this.mContext).inflate(R.layout.item_listview_menu, parent, false);
                holder.iconImageView = (ImageView) v.findViewById(R.id.icon_imageview);
                holder.titleTextView = (TextView) v.findViewById(R.id.title_textview);
                holder.iconImageView.setImageResource(this.mListMenuItem.get(position).getImageResource());
                holder.titleTextView.setText(this.mListMenuItem.get(position).getTitle());
                break;
        }
        return v;
    }

    public class Holder {
        public TextView titleTextView;
        public ImageView iconImageView;
        public TextView emailTextView;
        public AppCompatCheckBox appCompatCheckBox;
    }
}
