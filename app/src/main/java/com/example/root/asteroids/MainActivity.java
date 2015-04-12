package com.example.root.asteroids;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity implements Runnable {

    ListView lvStream;
    ArrayList<HashMap<String, String>> arrList;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Button CmrBtn;
    Typeface typeface;
    ImageView imageView;
    HashMap<String, String> map1 = new HashMap<String, String>();
    String sh_path=  "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        typeface = Typeface.createFromAsset(getAssets(), "ROBOTO.ttf");
        lvStream = (ListView) findViewById(R.id.listView);
        imageView = (ImageView)findViewById(R.id.imageView);
        arrList = new ArrayList<HashMap<String, String>>();
        String json_str = getJsonData();

        try {

            JSONObject jOb = new JSONObject(json_str);
            JSONArray jArray = jOb.optJSONArray("sky");

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = null;
                json = jArray.getJSONObject(i);
                map1 = new HashMap<String, String>();
                //map1.put("path", json.getString("path"));
                map1.put("time", json.getString("time"));
                map1.put("long", json.getString("long"));
                map1.put("lat", json.getString("lat"));
                map1.put("path", json.getString("path"));
                sh_path = json.getString("path");

                arrList.add(map1);
                arrList.add(map1);
                arrList.add(map1);
            }

        } catch (Exception e) {

        }
        if (!arrList.isEmpty()) {
            ListAdapter adapter = new SimpleAdapter(this, arrList, R.layout.list_item, new String[]{"time", "long", "lat"},
                    new int[]{R.id.tvName, R.id.tvDate, R.id.tvCoordinates});
            lvStream.setAdapter(adapter);
            //CreateBitmap();
        }
    }

    private String getJsonData() {

        Log.i("***", SplashAsteroids.file_info);
        return SplashAsteroids.file_info;
    }

    void CreateBitmap(){

        File imgFile = new  File(sh_path);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(sh_path);
            ImageView myImage = (ImageView) findViewById(R.id.imageView);
            myImage.setImageBitmap(myBitmap);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CmrBtn = (Button) findViewById(R.id.CameraBtn);
        CmrBtn.setTypeface(typeface);

        CmrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MainActivity.this, CameraActivity.class);
        takePictureIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(takePictureIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /*
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void run() {

    }
}
