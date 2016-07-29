package framgia.vn.framgiacrb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import framgia.vn.framgiacrb.R;

/**
 * Created by lethuy on 28/07/2016.
 */
public class PlaceActivity extends AppCompatActivity {

    private ListView mListPlace;
    private EditText mEditPlace;
    private Toolbar mToolbar;
    private final String MESSAGE = "MESSAGE";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_repeat);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEditPlace = (EditText) findViewById(R.id.edtPlace);
        mListPlace = (ListView) findViewById(R.id.lvPlace);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_attendee, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                String message = mEditPlace.getText().toString();
                Intent intent = new Intent();
                intent.putExtra(MESSAGE, message);
                setResult(2, intent);
                finish();
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
