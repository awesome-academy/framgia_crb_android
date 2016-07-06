package framgia.vn.framgiacrb.utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import framgia.vn.framgiacrb.R;


/**
 * Dialog Utils
 * Created by nghicv on 2/15/2016.
 */
public class DialogUtils {
    private static AlertDialog sAlert;
    private static ProgressDialog sProgress;

    /**
     * Show alert message with message id
     */
    public static void showAlert(Context context, int msgId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msgId);
        builder.create().show();
    }

    /**
     * Show alert message with message id
     */
    public static void showErrorAlert(Context context, String message) {
        dismissProgressDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(R.string.title_error);
        builder.setPositiveButton(R.string.ok, null);
        builder.create().show();
    }

    /**
     * Show dialog progress.
     *
     * @param activity the context is running.
     */
    public static void showProgressDialog(final Activity activity) {
        try {
            if (activity != null && !activity.isFinishing()) {
                if (sAlert != null && sAlert.isShowing()) {
                    sAlert.dismiss();
                }
                if (sProgress != null && sProgress.isShowing()) {
                    sProgress.dismiss();
                }
                sProgress = new ProgressDialog(activity);
                sProgress.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showProgressDialog(final Context context) {
        try {
            if (sAlert != null && sAlert.isShowing()) {
                sAlert.dismiss();
            }
            if (sProgress != null && sProgress.isShowing()) {
                sProgress.dismiss();
            }
            sProgress = new ProgressDialog(context);
            sProgress.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dismiss progress.
     *
     * @param activity the context is running.
     */
    public static void dismissProgressDialog(final Activity activity) {
        try {
            if (activity != null && !activity.isFinishing()) {
                if (sProgress != null && sProgress.isShowing()) {
                    sProgress.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissProgressDialog() {
        try {
            if (sProgress != null && sProgress.isShowing()) {
                sProgress.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAlertAction(Context context, int message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(message));
        builder.setPositiveButton(R.string.ok, listener);
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    public static void showAlertAction(Context context, int message, DialogInterface.OnClickListener possitiveListener, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(message));
        builder.setPositiveButton(R.string.ok, possitiveListener);
        builder.setNegativeButton(R.string.cancel, negativeListener);
        builder.create().show();
    }

}
