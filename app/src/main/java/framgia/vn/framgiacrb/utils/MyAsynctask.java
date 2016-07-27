package framgia.vn.framgiacrb.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import framgia.vn.framgiacrb.R;

/**
 * Created by lethuy on 26/07/2016.
 */
public abstract class MyAsynctask<T1, T2, T3> extends AsyncTask<T1, T2, T3> {
    protected Context mContext;
    protected ProgressDialog mProgressDialog;

    public MyAsynctask(Context context) {
        this.mContext = context;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getString(R.string.msg_wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    protected void onPostExecute(T3 result) {
        mProgressDialog.dismiss();
    }
}