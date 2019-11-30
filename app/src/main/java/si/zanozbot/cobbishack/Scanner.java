package si.zanozbot.cobbishack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Scanner extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;
    private static String TAG = "COBBIS_HACKATHON";
    private List<BarcodeFormat> mFormats = new ArrayList<>();
    private CobbisService cobbisService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_scanner);


        // Initialization of Cobbis Service
        cobbisService = RetrofitClientInstance.getRetrofitInstance().create(CobbisService.class);

        mFormats.add(BarcodeFormat.UPCA);
        mFormats.add(BarcodeFormat.CODE39);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 100);
        }

        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    private void sendScannedCode(final String number) {
        Call<CobbisModel> call = cobbisService.scan(number);
        call.enqueue(new Callback<CobbisModel>() {
            @Override
            public void onResponse(Call<CobbisModel> call, Response<CobbisModel> response) {
                handleResponseStatus(response.body());
                updateData(response.body(), number);
            }

            @Override
            public void onFailure(Call<CobbisModel> call, Throwable t) {
                MediaPlayer sound = MediaPlayer.create(Scanner.this, R.raw.napaka);
                sound.start();
            }
        });
    }

    private void handleResponseStatus(CobbisModel response) {
        Log.d(TAG, response.getStatus().toString());

        MediaPlayer sound = MediaPlayer.create(Scanner.this, R.raw.napaka);

        switch (response.getStatus()) {
            case 0: {
                sound = MediaPlayer.create(Scanner.this, R.raw.ok);
                break;
            }
            case 12: {
                sound = MediaPlayer.create(Scanner.this, R.raw.izposojeno);
                break;
            }
            case 11: {
                sound = MediaPlayer.create(Scanner.this, R.raw.duplikat);
                break;
            }
        }

        sound.start();

        mScannerView.resumeCameraPreview(this);
    }

    private void updateData(CobbisModel model, String number) {
        ArrayList<DataModel> list = DataSingleton.instance.getState();
        list.add(new DataModel(model.getMessage(), number));
        DataSingleton.instance.setState(list);
    }

    private void showNumber(String number) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, "Prebrana številka: " + number, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action_more, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mScannerView.stopCamera();
                        setResult(RESULT_OK);
                        finish();
                    }
                });
        snackbar.show();
    }

    @Override
    public void handleResult(Result rawResult) {
        String number = rawResult.getContents();
        BarcodeFormat format = rawResult.getBarcodeFormat();

        if (format == BarcodeFormat.UPCA) {
            number = number.substring(0, number.length() - 1);
        }

        this.showNumber(number);
        this.sendScannedCode(number);
    }
}
