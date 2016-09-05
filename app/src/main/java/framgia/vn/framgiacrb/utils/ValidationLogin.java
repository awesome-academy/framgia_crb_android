package framgia.vn.framgiacrb.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import framgia.vn.framgiacrb.R;

/**
 * Created by lethuy on 26/07/2016.
 */
public class ValidationLogin {


    public static final int PASSWORD_MIN_LENGTH = 8;

    public static boolean isValidEmail(Context context, EditText editTextEmail) {
        CharSequence target = editTextEmail.getText().toString();
        if (TextUtils.isEmpty(target)) {
            editTextEmail.setError(context.getString(R.string.error_email_blank));
            return false;
        } else {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
                return true;
            } else {
                editTextEmail.setError(context.getString(R.string.error_email_invalid));
                return false;
            }
        }
    }

    public static boolean isValidatePassword(Context context, EditText editTextPasssword) {
        boolean isValid = false;
        String password = editTextPasssword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editTextPasssword.setError(context.getString(R.string.error_password_blank));
        } else if (password.length() < PASSWORD_MIN_LENGTH) {
            editTextPasssword.setError(context.getString(R.string.error_password_short));
        } else {
            isValid = true;
        }
        return isValid;
    }
}
