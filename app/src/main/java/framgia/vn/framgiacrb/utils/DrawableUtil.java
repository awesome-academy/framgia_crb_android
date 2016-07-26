package framgia.vn.framgiacrb.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

import java.util.Calendar;

/**
 * Created by framgia on 26/07/2016.
 */
public class DrawableUtil {
    private static final int TEXT_SIZE = 30;
    private static final int OFFSET_X = 20;
    private static final int OFFSET_Y = 15;
    public static String getTodayDay() {
        return Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    public static BitmapDrawable writeOnDrawable(Context context, int drawableId, String text){

        Bitmap bm = BitmapFactory
            .decodeResource(context.getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(TEXT_SIZE);
        paint.setAntiAlias(true);
        Canvas canvas = new Canvas(bm);
        canvas.drawText(text, OFFSET_X, bm.getHeight()/2 + OFFSET_Y, paint);
        return new BitmapDrawable(context.getResources(), bm);
    }
}
