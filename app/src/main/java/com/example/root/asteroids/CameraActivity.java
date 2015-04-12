package com.example.root.asteroids;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class CameraActivity extends Activity implements SensorEventListener {

    private Camera cameraObject;
    TextView sensorTV;
    boolean position_is_right = false;


    Button btn_capture;

    public Camera initCam() {
        Camera object = null;
        try {
            object = Camera.open();
            object.setDisplayOrientation(90);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    private PictureCallback capturedIt = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Log.i("shit", "happens");
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            if (bitmap == null) {
                Toast.makeText(getApplicationContext(), "not taken", Toast.LENGTH_SHORT).show();
            } else {

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 20, stream);
                byte[] bytes = stream.toByteArray();
                Intent intent = new Intent(CameraActivity.this, EditorActivity.class);
                intent.putExtra("bmp", bytes);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CameraActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        cameraObject.release();
        finish();
        startActivity(intent);
    }

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Sensor mOrientation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraObject.release();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraObject = initCam();
        ShowCamera showCamera = new ShowCamera(this, cameraObject);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(showCamera);
        btn_capture = (Button) findViewById(R.id.button_capture);
        sensorTV = (TextView) findViewById(R.id.sensorTV);
        btn_capture.bringToFront();
        sensorTV.bringToFront();

        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position_is_right) {
                    cameraObject.takePicture(null, null, capturedIt);
                }
            }
        });

        mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);

    }

    void ProcessSensors(float pitch_angle) {

        sensorTV.setText("angle: " + (pitch_angle + 180));


        if (pitch_angle < -130 && pitch_angle > -140) {
            sensorTV.setTextColor(Color.GREEN);
            position_is_right = true;
        } else {
            sensorTV.setTextColor(Color.RED);
            position_is_right = false;
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float azimuth_angle = event.values[0];
        float pitch_angle = event.values[1];
        float roll_angle = event.values[2];
        ProcessSensors(pitch_angle);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

