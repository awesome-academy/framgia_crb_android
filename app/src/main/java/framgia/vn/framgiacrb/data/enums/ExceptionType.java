package framgia.vn.framgiacrb.data.enums;

import android.text.TextUtils;

/**
 * Created by framgia on 14/09/2016.
 */
public enum ExceptionType {
    DELETE_ONLY("delete_only"),
    EDIT_ONLY("edit_only"),
    EDIT_ALL_FOLLOW("edit_all_follow");
    private String mValues;

    ExceptionType(String values) {
        mValues = values;
    }

    public String getValues() {
        return mValues;
    }

    public static ExceptionType getExceptionType(String values) {
        for (ExceptionType exceptionType : ExceptionType.values())
            if (TextUtils.equals(exceptionType.getValues(), values)) return exceptionType;
        return ExceptionType.DELETE_ONLY;
    }
}
