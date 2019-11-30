package si.zanozbot.cobbishack;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    private static String TAG = "COBBIS_HACKATHON";
    private CobbisService cobbisService;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.my_list_view);
        ArrayList<String> list = new ArrayList<String>();
        DataSingleton.instance.setState(list);

        CustomArrayAdapter mAdapter = new CustomArrayAdapter(this, DataSingleton.instance.getState());
        listView.setAdapter(mAdapter);

        // Initialization of Cobbis Service
        cobbisService = RetrofitClientInstance.getRetrofitInstance().create(CobbisService.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Scanner.class);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        CustomArrayAdapter mAdapter = new CustomArrayAdapter(this, DataSingleton.instance.getState());
        listView.setAdapter(mAdapter);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void startScanning() {
        Call<CobbisModel> call = cobbisService.start();
        call.enqueue(new Callback<CobbisModel>() {
            @Override
            public void onResponse(Call<CobbisModel> call, Response<CobbisModel> response) {
                Log.d(TAG, response.message());
                DataSingleton.instance.resetState();
                Intent intent = new Intent(MainActivity.this, Scanner.class);
                startActivityForResult(intent, 100);

            }

            @Override
            public void onFailure(Call<CobbisModel> call, Throwable t) {
                playError();
            }
        });
    }

    private void stopScanning() {
        Call<CobbisModel> call = cobbisService.stop();
        call.enqueue(new Callback<CobbisModel>() {
            @Override
            public void onResponse(Call<CobbisModel> call, Response<CobbisModel> response) {
                Log.d(TAG, response.message());
            }

            @Override
            public void onFailure(Call<CobbisModel> call, Throwable t) {
                playError();
            }
        });
    }

    private void playError() {
        MediaPlayer sound = MediaPlayer.create(MainActivity.this, R.raw.napaka);
        sound.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_start) {
            this.startScanning();
            return true;
        }

        if (id == R.id.action_stop) {
            this.stopScanning();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
