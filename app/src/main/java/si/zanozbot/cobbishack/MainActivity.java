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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    private static String TAG = "COBBIS_HACKATHON";
    private CobbisService cobbisService;
    private ListView listView;
    private LinearLayout mLinearLayout;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLinearLayout = findViewById(R.id.no_scans_layout);

        listView = findViewById(R.id.my_list_view);
        ArrayList<DataModel> list = new ArrayList<DataModel>();
        DataSingleton.instance.setState(list);

        CustomArrayAdapter mAdapter = new CustomArrayAdapter(this, DataSingleton.instance.getState());
        listView.setAdapter(mAdapter);

        // Initialization of Cobbis Service
        cobbisService = RetrofitClientInstance.getRetrofitInstance().create(CobbisService.class);

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DataSingleton.instance.getState().size() > 0) {
                    Intent intent = new Intent(MainActivity.this, Scanner.class);
                    startActivityForResult(intent, 100);
                } else {
                    startScanning();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        ArrayList<DataModel> dataModels = DataSingleton.instance.getState();

        if (dataModels.size() > 0) {
            this.mLinearLayout.setVisibility(View.INVISIBLE);
        } else {
            this.mLinearLayout.setVisibility(View.VISIBLE);
        }

        CustomArrayAdapter mAdapter = new CustomArrayAdapter(this, dataModels);
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
                if (response.body().getStatus() == 21) {
                    Toast.makeText(MainActivity.this, "Vse kode niso bile skenirane.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Skeniranje se je zaključilo.", Toast.LENGTH_LONG).show();
                }
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
