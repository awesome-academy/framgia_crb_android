package framgia.vn.framgiacrb.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import framgia.vn.framgiacrb.R;

/**
 * Created by nghicv on 29/07/2016.
 */
public class TimeLineView extends View {

    private float mWidth;
    private float mHeight;
    private Paint mPaint;

    public TimeLineView(Context context) {
        super(context);
    }

    public TimeLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(context.getResources().getColor(R.color.colorAccent));
    }

    public TimeLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mHeight/2, mHeight/2, mHeight/2, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawLine(0, mHeight/2, mWidth, mHeight/2, mPaint);
    }
}
