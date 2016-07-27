package framgia.vn.framgiacrb.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by lethuy on 26/07/2016.
 */
public class DialogUtil {
    public static Dialog showDialog(Context context, String title, String message,
                                    String textPositiveButton, String texNegativeButton,
                                    DialogInterface.OnClickListener positiveClickListener,
                                    DialogInterface.OnClickListener negativeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setCancelable(false);
        if (title != null) {
            builder.setTitle(title);
        }
        if (message != null) {
            builder.setMessage(message);
        }
        if (positiveClickListener != null) {
            builder.setPositiveButton(textPositiveButton, positiveClickListener);
        }
        if (negativeClickListener != null) {
            builder.setNegativeButton(texNegativeButton, negativeClickListener);
        }
        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        alert.show();
        return alert;
    }

    public static void showAlert(Context context, int msgId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msgId);
        builder.create().show();
    }
}