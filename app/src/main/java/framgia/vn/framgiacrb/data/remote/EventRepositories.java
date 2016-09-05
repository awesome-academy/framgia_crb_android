package framgia.vn.framgiacrb.data.remote;

import android.content.Context;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.asyntask.RegisterNotificationAsyncTask;
import framgia.vn.framgiacrb.data.EventRepository;
import framgia.vn.framgiacrb.data.OnLoadEventListener;
import framgia.vn.framgiacrb.data.local.EventRepositoriesLocal;
import framgia.vn.framgiacrb.data.model.Calendar;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.data.model.ResposeDTO;
import framgia.vn.framgiacrb.network.ServiceBuilder;
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
                        RegisterNotificationAsyncTask registerNotificationAsynTask
                            = new RegisterNotificationAsyncTask();
                        registerNotificationAsynTask.execute(mEvents);
                    } else {
                        mOnLoadEventListener.onSuccess();
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.message_error), Toast.LENGTH_SHORT).show();
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

    @Override
    public Event getEventById(int id) {
        return null;
    }

    @Override
    public RealmResults<Event> getEventByDate(Date date) {
        return null;
    }
}
