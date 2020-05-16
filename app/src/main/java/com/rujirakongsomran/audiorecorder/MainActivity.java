package com.rujirakongsomran.audiorecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.ornach.nobobutton.NoboButton;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    NoboButton btnStartRecord;
    NoboButton btnStopRecord;
    NoboButton btnStartPlay;
    NoboButton btnStopPlay;
    String pathSave = "";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    final int REQUEST_PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstances();

        // From Android M, you need request Run-time permission
        if (checkPermissionFromDevice()) {

            btnStartRecord.setOnClickListener(btnStartRecordListener);

            btnStopRecord.setOnClickListener(btnStopRecordListener);

            btnStartPlay.setOnClickListener(btnStartPlayListener);



        } else {
            requestPermission();
        }

    }

    private void setupMediaRecorder() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);
    }

    private boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }

    private void initInstances() {
        btnStartRecord = (NoboButton) findViewById(R.id.btnStartRecord);
        btnStopRecord = (NoboButton) findViewById(R.id.btnStopRecord);
        btnStartPlay = (NoboButton) findViewById(R.id.btnStartPlay);
        btnStopPlay = (NoboButton) findViewById(R.id.btnStopPlay);
    }

    // Listener Zone
    View.OnClickListener btnStartRecordListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            pathSave = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/"
                    + UUID.randomUUID().toString() + "_audio_record.3gp";
            setupMediaRecorder();
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Set Disable button
            btnStartPlay.setEnabled(false);
            btnStopPlay.setEnabled(false);

            Toast.makeText(MainActivity.this, "Recording...", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener btnStopRecordListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mediaRecorder.stop();

            btnStartRecord.setEnabled(true);
            btnStopRecord.setEnabled(false);

            btnStartPlay.setEnabled(true);
            btnStopPlay.setEnabled(false);
        }
    };

    View.OnClickListener btnStartPlayListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            btnStopPlay.setEnabled(true);
            btnStopRecord.setEnabled(false);
            btnStartRecord.setEnabled(false);

            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(pathSave);
                mediaPlayer.prepare();

            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            Toast.makeText(MainActivity.this, "Playing...", Toast.LENGTH_SHORT).show();
        }
    };
}
