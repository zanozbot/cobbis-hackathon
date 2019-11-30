package si.zanozbot.cobbishack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

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

    private void sendScannedCode(String number) {
        Call<CobbisModel> call = cobbisService.scan(number);
        call.enqueue(new Callback<CobbisModel>() {
            @Override
            public void onResponse(Call<CobbisModel> call, Response<CobbisModel> response) {
                handleResponseStatus(response.body());
            }

            @Override
            public void onFailure(Call<CobbisModel> call, Throwable t) {

            }
        });
    }

    private void handleResponseStatus(CobbisModel response) {
        Log.d(TAG, response.getStatus().toString());
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void handleResult(Result rawResult) {
        String number = rawResult.getContents();
        BarcodeFormat format = rawResult.getBarcodeFormat();

        if (format == BarcodeFormat.UPCA) {
            number = number.substring(0, number.length() - 1);
        }

        this.sendScannedCode(number);
    }
}
