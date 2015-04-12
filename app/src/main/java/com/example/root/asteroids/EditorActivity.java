package com.example.root.asteroids;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditorActivity extends ActionBarActivity {
    ImageView photo_dis, color_v;
    Button btn_done;
    String js_msg = "";
    double _long, _lat;
    private Bitmap bmp_save;
    GPSTracker gps;
    TextView st, red_txt,green_txt,blue_txt;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        photo_dis = (ImageView) findViewById(R.id.editor_image);
        color_v = (ImageView) findViewById(R.id.color_v);
        btn_done = (Button) findViewById(R.id.done);
        red_txt =  (TextView)findViewById(R.id.red_txt);
        green_txt= (TextView)findViewById(R.id.green_txt);
        blue_txt = (TextView)findViewById(R.id.blue_txt);

        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("bmp");
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        st = (TextView) findViewById(R.id.st);
        bmp = rotate(bmp, 90);
        add_data_to_photo(bmp);
        photo_dis.setImageBitmap(bmp);
        get_dominant_color(bmp);
        bmp_save = bmp;
        typeface = Typeface.createFromAsset(getAssets(), "ROBOTO.ttf");


    }


    private void add_data_to_photo(Bitmap local_bmp) {
        gps = new GPSTracker(this);
        if (gps.canGetLocation()) { // gps enabled} // return boolean true/false
            _lat = gps.getLatitude();
            _long = gps.getLongitude();
        } else {
            gps.stopUsingGPS();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String timestamp = sdf.format(new Date());
        writeTextOnDrawable(local_bmp, "" + _lat, 0, 0);
        writeTextOnDrawable(local_bmp, "" + _long, 0, 30);
        writeTextOnDrawable(local_bmp, timestamp, 0, 60);
    }

    private void save_to_file(Bitmap bmp) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String timestamp = sdf.format(new Date());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM" + File.separator + "img" + timestamp + ".jpg");
        try {
            f.createNewFile();
            //write the bytes in file
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            // remember close de FileOutput
            fo.close();
        } catch (Exception e) {

        }

        JSONObject skyImage = new JSONObject();
        try {
            skyImage.put("path", Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/DCIM" + File.separator + "img" + timestamp + ".jpg");
            skyImage.put("long", gps.getLongitude());
            skyImage.put("lat", gps.getLatitude());
            skyImage.put("time", timestamp);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject skyImage2 = new JSONObject();
        try {
            skyImage.put("path", Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/DCIM" + File.separator + "img" + timestamp + ".jpg");
            skyImage.put("long", gps.getLongitude());
            skyImage.put("lat", gps.getLatitude());
            skyImage.put("time", timestamp);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();

        jsonArray.put(skyImage);
        jsonArray.put(skyImage2);


        JSONObject skyImages = new JSONObject();
        try {
            //skyImages.put(skyImage);
            skyImages.put("sky", jsonArray);
        } catch (Exception e) {

        }

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "temp" + File.separator + "sky.txt");

        String jsonStr = skyImages.toString();
        Log.i("jsonString: ", jsonStr);

        if (!file.exists()) {

            writeStringAsFile(jsonStr, file);
        } else {
            writeStringAsFile(jsonStr, file);
            //appendStringToFile(jsonStr, file);
        }
    }

    // Object for intrinsic lock (per docs 0 length array "lighter" than a normal Object
    public static final Object[] DATA_LOCK = new Object[0];

    public static boolean writeStringAsFile(final String fileContents, final File file) {
        boolean result = false;
        try {
            synchronized (DATA_LOCK) {
                if (file != null) {
                    file.createNewFile(); // ok if returns false, overwrite
                    Writer out = new BufferedWriter(new FileWriter(file), 2048);
                    out.write(fileContents);
                    out.close();
                }
                result = true;
            }
        } catch (IOException e) {
            // Log.e(Constants.LOG_TAG, "Error writing string data to file " + e.getMessage(), e);
        }
        return result;
    }

    public static boolean appendStringToFile(final String appendContents, final File file) {
        boolean result = false;
        try {
            synchronized (DATA_LOCK) {
                if (file != null && file.canWrite()) {
                    file.createNewFile(); // ok if returns false, overwrite
                    Writer out = new BufferedWriter(new FileWriter(file, true), 1024);
                    out.write(appendContents);
                    out.close();
                    result = true;
                }
            }
        } catch (IOException e) {
            //   Log.e(Constants.LOG_TAG, "Error appending string data to file " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_to_file(bmp_save);
                Intent intent = new Intent(EditorActivity.this, SplashAsteroids.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    public static Bitmap rotate(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditorActivity.this, CameraActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private BitmapDrawable writeTextOnDrawable(Bitmap bm, String text1, int x, int y) {

        //Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
        //.copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.create("Roboto", Typeface.BOLD);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(getBaseContext(), 11));
        Rect textRect = new Rect();
        paint.getTextBounds(text1, 0, text1.length(), textRect);
        Canvas canvas = new Canvas(bm);
        //If the text is bigger than the canvas , reduce the font size
        if (textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(getBaseContext(), 7));        //Scaling needs to be used for different dpi's
        //Calculate the positions
        //int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset
        int xPos = (canvas.getWidth()) - 80;
        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        //int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
        int yPos = (int) (canvas.getHeight()) - (canvas.getHeight() - 60 - y);
        canvas.drawText(text1, xPos - x, yPos, paint);
        return new BitmapDrawable(getResources(), bm);
    }

    public static int convertToPixels(Context context, int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f);

    }

    private void get_dominant_color(Bitmap bmp) {
        Bitmap onePixelBitmap = Bitmap.createScaledBitmap(bmp, 1, 1, true);
        int pixel = onePixelBitmap.getPixel(0, 0);
        final int redValue = Color.red(pixel);
        final int blueValue = Color.blue(pixel);
        final int greenValue = Color.green(pixel);
        st.setTypeface(typeface);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                red_txt.setText(""+redValue);

                green_txt.setText(""+greenValue);

                blue_txt.setText(""+blueValue);


            }
        });

        if ((blueValue > 235) && (redValue > 0 && redValue < 135) &&
                (greenValue > 191 && greenValue < 206)) {

            st.setBackgroundColor(Color.BLUE);
            st.setTextColor(Color.WHITE);
            st.setText("Sky is \n DeepBlue!");

        } else if ((blueValue > 235 && blueValue < 250) && (redValue > 135 && redValue < 207) &&
                (greenValue > 206 && greenValue < 236)) {
            st.setBackgroundColor(Color.BLUE);
            st.setTextColor(Color.WHITE);
            st.setText("Sky is \n Blue!");

        } else if ((blueValue > 236 && blueValue < 250) && (redValue > 135 && redValue < 207) &&
                (greenValue > 206 && greenValue < 236)) {
            st.setBackgroundColor(Color.BLUE);
            st.setTextColor(Color.WHITE);
            st.setText("Sky is \n LightBlue!");

        } else if ((blueValue > 236 && blueValue < 255) && (redValue > 207 && redValue < 254) &&
                (greenValue > 236 && greenValue < 252)) {
            st.setBackgroundColor(Color.BLUE);
            st.setTextColor(Color.WHITE);
            st.setText("Sky is \n Lily!");

        } else if ((blueValue == 255) && (redValue >= 254) &&
                (greenValue >= 252)) {
            st.setBackgroundColor(Color.BLUE);
            st.setTextColor(Color.WHITE);
            st.setText("Sky is \n MilkyWhite!");

        } else {
            st.setText("");
        }
        color_v.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
    }
}
