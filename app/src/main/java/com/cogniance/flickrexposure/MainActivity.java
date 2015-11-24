package com.cogniance.flickrexposure;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class MainActivity extends Activity {

    private final String LOG_TAG = this.getClass().getName();

    protected final static String IMAGE_URL = "com.cogniance.flickrexposure.IMAGE_URL";

    private Toolbar toolbar;

    private ProgressBar progressBar;
    private TextView progressText;

    private List<String> imagesArray;
    private List<String> titlesArray;

    private ListView listView;
    private ImageAdapter imageAdapter;

    private DownloadContentTask downloadContentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);

        initToolbar();
        initProgressBar();

        //Separated thread for connection establishment and content downloading
        downloadContentTask = (DownloadContentTask) new DownloadContentTask(this, progressBar, progressText).execute(FlickrUser.flickrURL);

        Log.d(LOG_TAG, "onCreate");
    }

    //Handling the configuration change yourself
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");

        if (downloadContentTask.getStatus() == AsyncTask.Status.FINISHED) {
            downloadContentTask.cancel(true);
            Log.d(LOG_TAG, "downloadContentTask canceled via onStop");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");

        if (downloadContentTask.getStatus() == AsyncTask.Status.RUNNING) {
            downloadContentTask.cancel(true);
            Log.d(LOG_TAG, "downloadContentTask canceled via onDestroy");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "onRestart");
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.settings) {
                    Toast.makeText(getApplicationContext(), R.string.settings_clicked, Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.download) {
                    Toast.makeText(getApplicationContext(), R.string.download_clicked, Toast.LENGTH_SHORT).show();

                    initListView();
                }
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu);

        Log.d(LOG_TAG, "initToolbar");
    }

    private void initProgressBar() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressText = (TextView) findViewById(R.id.progressText);
        progressText.setText(R.string.connection);
    }

    protected void initListView() {
        listView = (ListView) findViewById(R.id.listViewMain);
        imagesArray = downloadContentTask.getImageURLArray();
        titlesArray = downloadContentTask.getImageTitlesArray();
        imageAdapter = new ImageAdapter(getApplicationContext(), R.layout.imageitem, imagesArray, titlesArray);
        listView.setAdapter(imageAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "item is selected: position = " + position + ", id = " + id);

                startDisplayImageActivity(position);
            }
        });

        Log.d(LOG_TAG, "initListView");
    }

    private void startDisplayImageActivity(int position) {
        Intent intent = new Intent(this, DisplayImageActivity.class);
        String imageURL = imagesArray.get(position);
        intent.putExtra(IMAGE_URL, imageURL);
        startActivity(intent);
    }

}
