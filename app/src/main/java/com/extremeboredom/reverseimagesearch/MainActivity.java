package com.extremeboredom.reverseimagesearch;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.extremeboredom.reverseimagesearch.helpers.DocumentHelper;
import com.extremeboredom.reverseimagesearch.imgurmodel.ImageResponse;
import com.extremeboredom.reverseimagesearch.imgurmodel.Upload;
import com.extremeboredom.reverseimagesearch.services.UploadService;

import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static Activity instance = null;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Intent intent = getIntent();
        Uri receivedUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (receivedUri != null) {
            String filePath = DocumentHelper.getPath(this, receivedUri);
            File chosenFile = new File(filePath);
            Upload upload = new Upload();
            upload.image = chosenFile;
            upload.title = chosenFile.getName();
            UploadService uploadService = new UploadService(this);
            DecimalFormat df = new DecimalFormat("##.##");
            df.setRoundingMode(RoundingMode.DOWN);
            Toast.makeText(instance, "Uploading... Your file is " + df.format(chosenFile.length()/1048576f)+ "MB", Toast.LENGTH_LONG).show();
            uploadService.Execute(upload, new UiCallback());

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
           instance = this;

        if (getIntent().getAction() == Intent.ACTION_SEND) {
                verifyStoragePermissions(this);

            }
        }

    @Override
    protected void onDestroy() {
        instance = null;
        super.onDestroy();
    }

    private class UiCallback implements Callback<ImageResponse> {

        @Override
        public void success(ImageResponse imageResponse, Response response) {
            Log.d("", "");
            String URL = "https://www.google.com/searchbyimage?site=search&sa=X&image_url=" + imageResponse.data.link;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
            startActivity(browserIntent);
            instance.finish();
        }

        @Override
        public void failure(RetrofitError error) {
            //Assume we have no connection, since error is null
            Toast.makeText(instance, "Oops, No internet connection.", Toast.LENGTH_LONG).show();
        }
    }
}
