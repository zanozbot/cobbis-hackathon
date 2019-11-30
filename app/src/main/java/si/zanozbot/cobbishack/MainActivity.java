package si.zanozbot.cobbishack;

import android.content.Intent;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    private static String TAG = "COBBIS_HACKATHON";
    private CobbisService cobbisService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialization of Cobbis Service
        cobbisService = RetrofitClientInstance.getRetrofitInstance().create(CobbisService.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Scanner.class);
                startActivity(intent);
            }
        });
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
                Intent intent = new Intent(MainActivity.this, Scanner.class);
                startActivity(intent);

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
