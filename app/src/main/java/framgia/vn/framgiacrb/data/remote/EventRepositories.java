package framgia.vn.framgiacrb.data.remote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.activity.LoginActivity;
import framgia.vn.framgiacrb.activity.MainActivity;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.EventRepository;
import framgia.vn.framgiacrb.data.OnLoadEventListener;
import framgia.vn.framgiacrb.data.local.EventRepositoriesLocal;
import framgia.vn.framgiacrb.data.model.Calendar;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.data.model.ResposeDTO;
import framgia.vn.framgiacrb.network.ServiceBuilder;
import framgia.vn.framgiacrb.utils.Utils;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nghicv on 26/07/2016.
 */
public class EventRepositories implements EventRepository{

    private List<Event> mEvents;
    private OnLoadEventListener mOnLoadEventListener;

    public void setOnLoadEventListener(OnLoadEventListener onLoadEventListener) {
        mOnLoadEventListener = onLoadEventListener;
    }

    @Override
    public void insertEvent(Event event) {

    }

    @Override
    public void deleteEvent(Event event) {

    }

    @Override
    public void updateEvent(Event event) {

    }

    @Override
    public RealmResults<Event> getAllEvents(int userId) {
        return null;
    }

    @Override
    public void getEventsByCalendar(String token, Calendar calendar, final Context context) {
        int id = calendar.getId();
        ServiceBuilder.getService().listEvents(token, id).enqueue(new Callback<ResposeDTO>() {
            @Override
            public void onResponse(Call<ResposeDTO> call, Response<ResposeDTO> response) {
                response.body();
                if (response.isSuccessful()) {
                    mEvents = response.body().getEvents();
                    if (mEvents != null) {
                        Realm realm = Realm.getDefaultInstance();
                        new EventRepositoriesLocal(realm).addEvents(mEvents, mOnLoadEventListener);
                    } else {
                        mOnLoadEventListener.onSuccess();
                    }
                } else {
                    String error = null;
                    try {
                        error = Utils.getStringFromJson(response.errorBody().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (error.equals(Constant.NOT_AUTHENTICATION)) {
                        logout(context);
                        ((MainActivity)context).finish();
                    } else {
                        Toast.makeText(context, context.getString(R.string.message_error), Toast.LENGTH_SHORT).show();
                    }
                    mOnLoadEventListener.onSuccess();
                }
            }

            @Override
            public void onFailure(Call<ResposeDTO> call, Throwable t) {
                if (mOnLoadEventListener != null) {
                    mOnLoadEventListener.onSuccess();
                }
                Toast.makeText(context, context.getString(R.string.message_not_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout(final Context context) {
        Toast.makeText(context, Constant.MESSAGE_NOT_AUTHENTICATION, Toast.LENGTH_SHORT).show();
        new EventRepositoriesLocal(Realm.getDefaultInstance()).clearDatabase(new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, R.string.message_logout_success, Toast.LENGTH_SHORT).show();
            }
        });
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SHAREPREFF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ((AppCompatActivity)context).finish();
    }

    @Override
    public Event getEventById(int id) {
        return null;
    }

    @Override
    public RealmResults<Event> getEventByDate(Date date) {
        return null;
    }
}
