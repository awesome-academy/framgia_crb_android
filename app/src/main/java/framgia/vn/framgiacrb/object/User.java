package framgia.vn.framgiacrb.object;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lethuy on 26/07/2016.
 */
public class User {
    @SerializedName("id")
    int mId;
    @SerializedName("name")
    String mName;

    public User(int id, String name ) {
        this.mId = id;
        this.mName = name;
    }
}
