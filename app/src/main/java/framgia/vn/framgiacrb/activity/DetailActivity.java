package framgia.vn.framgiacrb.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.object.ApiClient;
import framgia.vn.framgiacrb.object.ApiInterface;
import framgia.vn.framgiacrb.object.EventDetailResponse;
import framgia.vn.framgiacrb.utils.NetworkUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lucky_luke on 7/5/2016.
 */
public class DetailActivity extends AppCompatActivity {
    private TextView mTextViewBegin;
    private TextView mTextViewEnd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (NetworkUtil.isInternetConnected(getApplicationContext())) {
            loadData();
        }
    }

    private void loadData() {
        int id = 30;
        //MainActivity.sAuthToken = "5xb-xDYuiJSteJ-KCiWU";
        ApiInterface apiService =
            ApiClient.getClient().create(ApiInterface.class);
        Call<EventDetailResponse> call = apiService.getEventDetails(id, MainActivity.sAuthToken);
        call.enqueue(new Callback<EventDetailResponse>() {
            @Override
            public void onResponse(Call<EventDetailResponse> call,
                                   Response<EventDetailResponse> response) {
                EventDetailResponse eventDetailResponse = response.body();
                findView(eventDetailResponse.getEvent());
            }

            @Override
            public void onFailure(Call<EventDetailResponse> call, Throwable t) {
            }
        });
    }

    private void findView(Event event) {
        TextView description = (TextView)findViewById(R.id.title_description);
        description.setText(event.getDescription() == null ? "" : event.getDescription());
        TextView startDate = (TextView)findViewById(R.id.txt_DateStart);
        startDate.setText(event.getStartTime()== null ? "" :event.getStartTime().toString());
        TextView endDate = (TextView)findViewById(R.id.txt_DateFinish);
        endDate.setText(event.getEndDate()== null ? "" :event.getEndDate().toString());
        TextView title = (TextView) findViewById(R.id.title_event);
        title.setText(event.getTitle() == null ? "" : event.getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_edit:
                Intent intent = new Intent(this, EditActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle(R.string.question);
                builder.setMessage(R.string.delete);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.create().show();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
