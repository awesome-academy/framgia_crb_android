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
    private static final float TEXT_ZOOM = 0.4f;
    private static final float OFFSET_Y = 0.7f;
    public static String getTodayDay() {
        return Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    public static BitmapDrawable writeOnDrawable(Context context, int drawableId, String text){

        Bitmap bm = BitmapFactory
                .decodeResource(context.getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        float textSize = bm.getHeight() *TEXT_ZOOM;
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        Canvas canvas = new Canvas(bm);
        float offsetX = (bm.getWidth() - textSize) / 2;
        canvas.drawText(text, offsetX, bm.getHeight()*OFFSET_Y, paint);
        return new BitmapDrawable(context.getResources(), bm);
    }
}
