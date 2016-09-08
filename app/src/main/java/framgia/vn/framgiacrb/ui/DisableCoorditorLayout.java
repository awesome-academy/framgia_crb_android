package framgia.vn.framgiacrb.ui;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by lucky_luke on 9/5/2016.
 */
public class DisableCoorditorLayout extends CoordinatorLayout{
    private boolean mPassScrolling = true;

    public DisableCoorditorLayout(Context context) {
        super(context);
    }

    public DisableCoorditorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisableCoorditorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return mPassScrolling && super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    public void setPassScrolling(boolean passScrolling) {
        mPassScrolling = passScrolling;
    }
}
