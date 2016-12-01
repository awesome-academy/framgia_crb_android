package framgia.vn.framgiacrb.ui.widget;
/**
 * Created by lucky_luke on 7/18/2016.
 */
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class WrapContentHeightViewPager extends ViewPager {
    private int mHeight;
    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapContentHeightViewPager(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mHeight == 0) {
            View view = getChildAt(0);
            if (view != null) {
                view.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                mHeight = view.getMeasuredHeight();
                view.measure(widthMeasureSpec, mHeight);
            }
        }
        setMeasuredDimension(getMeasuredWidth(),
            MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
    }
}