package framgia.vn.framgiacrb.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.ui.MonthView;

/**
 * Created by lucky_luke on 7/18/2016.
 */
public class MonthToolbarPagerAdapter extends PagerAdapter {
    public static final int MIN_YEAR = 1970;
    public static final int MAX_YEAR = 2050;

    private Context mContext;

    public MonthToolbarPagerAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return (MAX_YEAR - MIN_YEAR) * 12;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //return super.instantiateItem(container, position);
        LayoutInflater inflater = LayoutInflater.from(this.mContext);
        View v = inflater.inflate(R.layout.month, container, false);
        MonthView mv = (MonthView) v.findViewById(R.id.item_month);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, MIN_YEAR + position / 12);
        calendar.set(Calendar.MONTH, position % 12);
        mv.setTime(calendar);
        mv.setMatchParent(false);
        container.addView(v);
        mv.setTag("month"+position);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
